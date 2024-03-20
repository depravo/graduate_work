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
        catalog = session.query(Product).all()
        catalog_json = []
        for product in catalog:
            catalog_json.append(
                {
                    "product_id": product.product_id,
                    "product_name": product.product_name,
                    "img_resource": product.img_resource,
                    "description": product.description,
                    "price": product.price,
                }
            )
        response = json.dumps(catalog_json, ensure_ascii=False)
        return response


@app.route("/api/v1/categories", methods=["GET"])
def getCategories():
    with Session() as session:
        categories = session.query(Category).all()
        categories_json = []
        for category in categories:
            categories_json.append(
                {
                    "category_name": category.category_name,
                }
            )
        response = json.dumps(categories_json, ensure_ascii=False)
        print(response)
        return response


@app.route("/api/v1/catalog/<category_name>/products", methods=["GET"])
def getCategoryProducts(category_name):
    category_name = request.args.get(
        "category_name"
    )  # Получаем параметр запроса category_name
    with Session() as session:
        # Получаем категорию по имени
        category = (
            session.query(Category).filter_by(category_name=category_name).first()
        )

        if category:
            # Получаем все товары, принадлежащие категории с заданным category_id
            catalog = (
                session.query(Product).filter_by(category_id=category.category_id).all()
            )
            catalog_json = []
            for product in catalog:
                catalog_json.append(
                    {
                        "product_id": product.product_id,
                        "product_name": product.product_name,
                        "img_resource": product.img_resource,
                        "description": product.description,
                        "price": product.price,
                    }
                )
            response = json.dumps(catalog_json, ensure_ascii=False)
            return response
        return jsonify()


def checkCredentials(username, password=None):
    if password is None:
        with Session() as session:
            results = session.query(User).filter(User.email == username).all()
            if len(results) > 0:
                return -1
            else:
                return 0
    else:
        with Session() as session:
            results = (
                session.query(User)
                .filter(and_(User.email == username, User.password == password))
                .all()
            )
            if len(results) != 1:
                return -1
            else:
                return results[0].user_id


def addUser(username, password, fname, lname, phone):
    with Session() as session:
        user = User(
            email=username,
            password=password,
            user_fname=fname,
            user_lname=lname,
            phone_number=phone,
        )
        session.add(user)
        max_id = session.query(func.max(User.user_id)).first()
        session.commit()
    return max_id[0]


@app.route("/api/v1/register", methods=["POST"])
def register():
    username = request.json.get("email")
    password = request.json.get("password")
    fname = request.json.get("fname")
    lname = request.json.get("lname")
    phone = request.json.get("phone_number")

    if checkCredentials(username=username) == -1:
        response = {
            "success": False,
            "message": "User with same name already exists.",
            "userId": None,
        }
    else:
        current_user_id = addUser(
            username=username,
            password=password,
            fname=fname,
            lname=lname,
            phone=phone,
        )
        response = {
            "success": True,
            "message": "Register and login successful!",
            "userId": current_user_id,
        }
    return json.dumps(response, ensure_ascii=False)


@app.route("/api/v1/signIn", methods=["POST"])
def signIn():
    username = request.json.get("email")
    password = request.json.get("password")
    current_user_id = checkCredentials(username=username, password=password)
    print(current_user_id)
    if current_user_id != -1:
        response = {
            "success": True,
            "message": "Login successful!",
            "userId": current_user_id,
        }
    else:
        response = {
            "success": False,
            "message": "Invalid username or password.",
            "userId": None,
        }
    return json.dumps(response, ensure_ascii=False)


@app.route("/api/v1/cart/<user_id>/items", methods=["GET"])
def getCartItems(user_id):
    with Session() as session:
        cart_items = (
            session.query(CartItem).join(Cart).filter(Cart.user_id == user_id).all()
        )
        items = []
        for cart_item in cart_items:
            product = session.query(Product).get(cart_item.product_id)
            items.append(
                {
                    "product_id": product.product_id,
                    "product_name": product.product_name,
                    "amount": cart_item.amount,
                }
            )
        return jsonify(items)


# Endpoint для добавления товара в корзину пользователя
@app.route("/api/v1/cart/<user_id>/items", methods=["POST"])
def addToCart(user_id):
    product_id = request.json.get("product_id")
    amount = request.json.get("amount")
    with Session() as session:
        cart = session.query(Cart).filter_by(user_id=user_id).first()
        if not cart:
            cart = Cart(user_id=user_id)
            session.add(cart)
            session.commit()

        cart_item = (
            session.query(CartItem)
            .filter_by(cart_id=cart.cart_id, product_id=product_id)
            .first()
        )
        if cart_item:
            cart_item.amount += amount
        else:
            cart_item = CartItem(
                cart_id=cart.cart_id, product_id=product_id, amount=amount
            )
            session.add(cart_item)

        session.commit()
        return jsonify({"success": True, "message": "Product added to cart."})


# Endpoint для удаления товара из корзины пользователя
@app.route("/api/v1/cart/<user_id>/items/<product_id>", methods=["DELETE"])
def removeFromCart(user_id, product_id):
    with Session() as session:
        cart = session.query(Cart).filter_by(user_id=user_id).first()
        if not cart:
            return jsonify({"success": False, "message": "Cart not found."})

        cart_item = (
            session.query(CartItem)
            .filter_by(cart_id=cart.cart_id, product_id=product_id)
            .first()
        )
        if not cart_item:
            return jsonify({"success": False, "message": "Product not found in cart."})

        session.delete(cart_item)
        session.commit()
        return jsonify({"success": True, "message": "Product removed from cart."})


# Endpoint для добавления товара в избранное пользователя
@app.route("/api/v1/favorite/<user_id>/items", methods=["POST"])
def addToFavorite(user_id):
    product_id = request.json.get("product_id")
    with Session() as session:
        favorite = session.query(Favorite).filter_by(user_id=user_id).first()
        if not favorite:
            favorite = Favorite(user_id=user_id)
            session.add(favorite)
            session.commit()

        favorite_item = (
            session.query(FavoriteItem)
            .filter_by(fav_id=favorite.fav_id, product_id=product_id)
            .first()
        )
        if not favorite_item:
            favorite_item = FavoriteItem(fav_id=favorite.fav_id, product_id=product_id)
            session.add(favorite_item)
            session.commit()

        return jsonify({"success": True, "message": "Product added to favorites."})


# Endpoint для удаления товара из избранного пользователя
@app.route("/api/v1/favorite/<user_id>/items/<product_id>", methods=["DELETE"])
def removeFromFavorite(user_id, product_id):
    with Session() as session:
        favorite = session.query(Favorite).filter_by(user_id=user_id).first()
        if not favorite:
            return jsonify({"success": False, "message": "Favorites not found."})

        favorite_item = (
            session.query(FavoriteItem)
            .filter_by(fav_id=favorite.fav_id, product_id=product_id)
            .first()
        )
        if not favorite_item:
            return jsonify(
                {"success": False, "message": "Product not found in favorites."}
            )

        session.delete(favorite_item)
        session.commit()
        return jsonify({"success": True, "message": "Product removed from favorites."})


# Endpoint для получения всех избранных товаров пользователя
@app.route("/api/v1/favorite/<user_id>/items", methods=["GET"])
def getFavoriteItems(user_id):
    with Session() as session:
        favorite_items = (
            session.query(FavoriteItem)
            .join(Favorite)
            .filter(Favorite.user_id == user_id)
            .all()
        )
        items = []
        for favorite_item in favorite_items:
            product = session.query(Product).get(favorite_item.product_id)
            items.append(
                {
                    "product_id": product.product_id,
                    "product_name": product.product_name,
                }
            )
        return jsonify(items)


# Endpoint для добавления товара в заказ пользователя
@app.route("/api/v1/order/<user_id>/items", methods=["POST"])
def addToOrder(user_id):
    product_id = request.json.get("product_id")
    amount = request.json.get("amount")
    with Session() as session:
        order = Order(user_id=user_id, order_date=datetime.now(), total_price=0)
        session.add(order)
        session.commit()

        order_item = OrderItem(
            order_id=order.order_id, product_id=product_id, amount=amount
        )
        session.add(order_item)
        session.commit()

        return jsonify({"success": True, "message": "Product added to order."})


# Endpoint для удаления товара из заказа пользователя
@app.route("/api/v1/order/<user_id>/items/<order_item_id>", methods=["DELETE"])
def removeFromOrder(user_id, order_item_id):
    with Session() as session:
        order_item = (
            session.query(OrderItem)
            .join(Order)
            .filter(Order.user_id == user_id, OrderItem.order_item_id == order_item_id)
            .first()
        )
        if not order_item:
            return jsonify({"success": False, "message": "Order item not found."})

        session.delete(order_item)
        session.commit()
        return jsonify({"success": True, "message": "Product removed from order."})


# Endpoint для получения полной информации о товаре, включая отзывы
@app.route("/api/v1/product/<product_id>", methods=["GET"])
def getProductInfo(product_id):
    with Session() as session:
        product = session.query(Product).get(product_id)
        print("1!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        print(product.product_name)
        print(product.description)
        if not product:
            return jsonify()

        #reviews = session.query(Review).filter(Review.product_id == 1).all()
        print("REVIERSRSERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        review_list = []
        # for review in reviews:
        #     print("REVIERSRSERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        #     user = session.query(User).filter(User.user_id == review.user_id).first()
        #     review_list.append(
        #         {
        #             "user_fname": user.user_fname,
        #             "user_lname": user.user_lname,
        #             "rate": review.rate,
        #             "comment": review.comment,
        #         }
        #     )
        return json.dumps(
            {
                "product_id": product.product_id,
                "product_name": product.product_name,
                "img_resource": product.img_resource,
                "description": product.description,
                "price": product.price,
                "rate": product.rate,
                "reviews": {},
            },
            ensure_ascii=False,
        )


@app.route("/api/v1/user/<user_id>", methods=["GET"])
def getUserInfo(user_id):
    session = Session()
    user = session.query(User).filter(User.user_id == user_id).first()
    session.close()
    if user:
        user_info = {
            "user_fname": user.user_fname,
            "user_lname": user.user_lname,
            "email": user.email,
            "phone_number": user.phone_number,
        }
        print(user_info)
        return json.dumps(user_info, ensure_ascii=False)
    else:
        return jsonify()


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
