
# A very simple Flask Hello World app for you to get started with...

from flask import Flask, request, jsonify
from flask_restful import reqparse, abort, Api, Resource
import requests
import os
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import create_engine
import os
import pandas as pd

app = Flask(__name__)
app.config["DEBUG"] = True

#Utworzenie bazy danych
basedir = os.path.abspath(os.path.dirname(__file__))

app.config['SQLALCHEMY_DATABASE_URI'] =\
        'sqlite:///' + os.path.join(basedir, 'database.db')
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db = SQLAlchemy(app)

class AppData(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    uzytkownik_id = db.Column(db.Integer, nullable=False)
    nazwa_aplikacji= db.Column(db.String(100), nullable=False)
    czas= db.Column(db.Integer, nullable=False)

    def __repr__(self):
        return f'<App {self.nazwa_aplikacji}>'

@app.route('/jsongetdata', methods=['POST']) #zbieranie danych z aplikacji
# klient wysyła POST na url: http://projektrozproszone.pythonanywhere.com/json-get_data
def json_get_data():
    request_data = request.get_json()

    user_id = None
    application_name = None
    time = None

    if request_data:
        if 'id' in request_data:
            user_id = request_data['id']

        if 'nazwa_aplikacji' in request_data:
            application_name = request_data['nazwa_aplikacji']

        if 'czas' in request_data:
            time = request_data['czas']
            # zapis do bazy danych
            app = AppData(  uzytkownik_id=user_id,
                            nazwa_aplikacji=application_name,
                            czas=time)

            db.session.add(app)
            db.session.commit()
    return ''


@app.route('/jsosendstats', methods=['POST'])#wysyłanie statystyk
# klient wysyła POST na url: http://projektrozproszone.pythonanywhere.com/json-send_stats
def json_send_stats():
    request_data = request.get_json()

    user_id = None

    if request_data:
        if 'id' in request_data:
            user_id = request_data['id']

            # przygotowywanie statystyk

            basedir = os.path.abspath(os.path.dirname(__file__))
            dbdir = 'sqlite:///' + os.path.join(basedir, 'database.db')
            engine = create_engine(dbdir)
            connection = engine.connect()

            sql="select nazwa_aplikacji, sum(czas) as sum_czas from app_data group by nazwa_aplikacji order by sum_czas DESC "
            df=pd.read_sql(sql,con=engine)
            df2=df.to_json(orient='records') #do jsona żeby wysłać

            #format wyjsciowy danych
            data = [{'nazwa_aplikacji':'', 'procent':''},{'nazwa_aplikacji':'', 'procent':''}]

    return df2

if __name__ == '__main__':
    # run app in debug mode
    app.run(port=5001)