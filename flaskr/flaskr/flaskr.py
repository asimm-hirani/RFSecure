import os
import sqlite3
from flask import Flask, request, session, g, redirect, url_for, abort, \
     render_template, flash

app = Flask(__name__)
app.config.from_object(__name__)

# Load default config and override config from an environment variable
app.config.update(dict(
    DATABASE=os.path.join(app.root_path, 'flaskr.db'),
    SECRET_KEY='dogeface'
))
app.config.from_envvar('FLASKR_SETTINGS', silent=True)

def connect_db():
    """Connects to the specific database."""
    rv = sqlite3.connect(app.config['DATABASE'])
    rv.row_factory = sqlite3.Row
    return rv

def init_db():
    db = get_db()
    with app.open_resource('schema.sql', mode='r') as f:
        db.cursor().executescript(f.read())
    db.commit()

@app.cli.command('initdb')
def initdb_command():
    """Initializes the database."""
    init_db()
    print('Initialized the database.')

def get_db():
    """Opens a new database connection if there is none yet for the
    current application context.
    """
    if not hasattr(g, 'sqlite_db'):
        g.sqlite_db = connect_db()
    return g.sqlite_db

@app.teardown_appcontext
def close_db(error):
    """Closes the database again at the end of the request."""
    if hasattr(g, 'sqlite_db'):
        g.sqlite_db.close()

@app.route('/')
def show_entries():
    db = get_db()
    cur = db.execute('select username, password from users order by id desc')
    entries = cur.fetchall()
    return render_template('show_entries.html', entries=entries)

@app.route('/add', methods=['POST'])
def add_visit():
    db = get_db()
    db.execute('insert into visitors (firstName, lastName, regTimestamp, image, idNum) values (?, ?, ?, ?, ?)',
                 [request.form['firstName'], request.form['lastName'], request.form['timestamp'], request.form['image'], request.form['id']])
    db.commit()
    return json.dumps({'success':True}), 200, {'ContentType':'application/json'}

@app.route('/insert', methods=['POST'])
def add_user():
    admin = None
    security = None
    worker = None
    if request.form['type'] == "admin":
        admin = "1"
        security = "0"
        worker = "0"
    elif request.form['type'] == "security":
        admin = "0"
        security = "1"
        worker = "0"
    else:
        admin = "0"
        security = "0"
        worker = "1"
    db = get_db()
    db.execute('insert into users (username, password, admin, security, worker) values (?, ?, ?, ?, ?)',
                 [request.form['username'], request.form['password'], admin, security, worker])
    db.commit()
    flash('New user was successfully added')
    return redirect(url_for('admin'))

@app.route('/login', methods=['POST'])
def login():
    cur = db.execute('select username, password, admin, security, worker from users order by id desc')
    users = cur.fetchall()
    for user in users:
        if user.username == request.form['username'] and user.password == request.form['password']:
            if user.admin == "1":
                return jsonify(response=2)
            elif user.security == "1":
                return jsonify(response=3)
            else:
                return jsonify(response=4)
        elif user.username == request.form['username'] and user.password != request.form['password']:
            return jsonify(response=1)
    return jsonify(response=0)
