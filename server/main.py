import json
import time
import pytz
import requests

from flask import Flask, request, jsonify

from DBHelper import *

app = Flask(__name__)
Session = sessionmaker(bind=engine)


@app.route("/api/v1")
def showHomePage():
    return "This is home page"


@app.route("/api/v1/catalog", methods=["GET"])
def getCatalog():
    with Session() as session:
        catalog = session.query(
            Product.product_name,
            Product.description,
            Product.img_resource,
            Product.price,
        ).all()
        catalog_json = []
        for row in catalog:
            catalog_json.append(
                {
                    "name": row[0],
                    "description": row[1],
                    "logoResource": row[2],
                    "price": row[3],
                }
            )
        response = json.dumps(catalog_json, ensure_ascii=False)
        return response

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
