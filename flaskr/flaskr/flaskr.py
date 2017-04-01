import os
import sqlite3
from flask import Flask, request, session, g, redirect, url_for, abort, \
     render_template, flash
from flask_bcrypt import Bcrypt

app = Flask(__name__)
bcrypt = Bcrypt(app)
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
def home():
    return render_template('actuallogin.html')

@app.route('/admin')
def admin():
    db = get_db()
    cur = db.execute('select username, password from users order by id desc')
    users = cur.fetchall()
    return render_template('admin.html', users=users)

@app.route('/add', methods=['POST'])
def add_visit():
    db = get_db()
    db.execute('insert into visitors (firstName, lastName, regTimestamp, image, idNum) values (?, ?, ?, ?, ?)',
                 [request.form['firstName'], request.form['lastName'], request.form['timestamp'], request.form['image'], request.form['id']])
    db.commit()
    return json.dumps({'success':True}), 200, {'ContentType':'application/json'}

@app.route('/profile', methods=['POST', 'GET'])
def profile():
    error = None
    admin = None
    security = None
    worker = None
    if request.method == 'POST':
        db = get_db()
        cur = db.execute('select username from users order by id desc')
        users = cur.fetchall()
        for row in users:
            if row[0] == request.form['username']:
                error = 'invalid username'
                db.commit()
                return render_template('profile.html', error=error)
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
        db.execute('insert into users (username, password, admin, security, worker) values (?, ?, ?, ?, ?)',
            [request.form['username'], bcrypt.generate_password_hash(request.form['password']).decode('utf-8'), admin, security, worker])
        db.commit()
        flash('New user was successfully added')
        return redirect(url_for('admin'))
    return render_template('profile.html', error=error)

@app.route('/actuallogin', methods=['POST'])
def actual_login():
    db = get_db()
    cur = db.execute('select username, password, admin, security, worker from users order by id desc')
    users = cur.fetchall()
    for row in users:
        if row[0] == request.form['username'] and bcrypt.check_password_hash(row[1], request.form['password']):
            if row[2] == "1":
                db.commit()
                session['logged_in'] = True
                flash('You were logged in')
                return redirect(url_for('admin'))
            elif row[3] == "1":
                db.commit()
                error = 'invalid credentials'
                return render_template('actuallogin.html', error=error)
            else:
                db.commit()
                error = 'invalid credentials'
                return render_template('actuallogin.html', error=error)
        elif row[0] == request.form['username'] and not bcrypt.check_password_hash(row[1], request.form['password']):
            db.commit()
            error = 'invalid password'
            return render_template('actuallogin.html', error=error)
    db.commit()
    error = 'invalid username'
    return render_template('actuallogin.html', error=error)

@app.route('/login', methods=['POST'])
def login():
    db = get_db()
    cur = db.execute('select username, password, admin, security, worker from users order by id desc')
    users = cur.fetchall()
    for row in users:
        if row[0] == request.form['username'] and bcrypt.check_password_hash(row[1], request.form['password']):
            if row[2] == "1":
                db.commit()
                return jsonify(response=2)
            elif row[3] == "1":
                db.commit()
                return jsonify(response=3)
            else:
                db.commit()
                return jsonify(response=4)
        elif row[0] == request.form['username'] and not bcrypt.check_password_hash(row[1], request.form['password']):
            db.commit()
            return jsonify(response=1)
    db.commit()
    return jsonify(response=0)

@app.route('/searchfirst', methods=['POST'])
def search_first():
    db = get_db()
    cur = db.execute('select firstName, lastName, regTimestamp, image, idNum from visitors order by id desc')
    vistors = cur.fetchall()
    vlist = []
    final = {}
    num = 0
    for row in vistors:
        if row[0] == request.form['firstName']:
            vlist.append(row)
    for visit in vlist:
        final[str(num)] = visit[0]
        num += 1
        final[str(num)] = visit[1]
        num += 1
        final[str(num)] = visit[2]
        num += 1
        final[str(num)] = visit[3]
        num += 1
        final[str(num)] = visit[4]
        num += 1
    db.commit()
    return jsonify(final)

@app.route('/searchlast', methods=['POST'])
def search_last():
    db = get_db()
    cur = db.execute('select firstName, lastName, regTimestamp, image, idNum from visitors order by id desc')
    vistors = cur.fetchall()
    vlist = []
    final = {}
    num = 0
    for row in vistors:
        if row[1] == request.form['lastName']:
            vlist.append(row)
    for visit in vlist:
        final[str(num)] = visit[0]
        num += 1
        final[str(num)] = visit[1]
        num += 1
        final[str(num)] = visit[2]
        num += 1
        final[str(num)] = visit[3]
        num += 1
        final[str(num)] = visit[4]
        num += 1
    db.commit()
    return jsonify(final)

@app.route('/searchnum', methods=['POST'])
def search_num():
    db = get_db()
    cur = db.execute('select firstName, lastName, regTimestamp, image, idNum from visitors order by id desc')
    vistors = cur.fetchall()
    vlist = []
    final = {}
    num = 0
    for vistor in vistors:
        if visitor[4] == request.form['idNum']:
            vlist.append(visitor)
    for visit in vlist:
        final[str(num)] = visit[0]
        num += 1
        final[str(num)] = visit[1]
        num += 1
        final[str(num)] = visit[2]
        num += 1
        final[str(num)] = visit[3]
        num += 1
        final[str(num)] = visit[4]
        num += 1
    db.commit()
    return jsonify(final)
