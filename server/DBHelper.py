from sqlalchemy import (
    create_engine,
    ForeignKey,
    Column,
    Integer,
    Float,
    String,
    and_,
    func,
)
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship
from sqlalchemy.orm import sessionmaker
from datetime import datetime


#connection_string = "mysql+mysqlconnector://depravo:22110.10@depravo.mysql.pythonanywhere-services.com/depravo$app_data"
connection_string = "sqlite:///online_shop.db"
engine = create_engine(connection_string, echo=True)
Base = declarative_base()
    
class User(Base):
    __tablename__ = "User"
    user_id = Column(Integer, primary_key=True)
    user_fname = Column(String)
    user_lname = Column(String)
    email = Column(String)
    password = Column(String)
    phone_number = Column(String)
    
class Address(Base):
    __tablename__ = "Address"
    address_id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey("User.user_id"))
    city = Column(String)
    street = Column(String)
    home = Column(Integer)
    flat_num = Column(Integer)

class Category(Base):
    __tablename__ = "Category"
    category_id = Column(Integer, primary_key=True)
    category_name = Column(String)

class Product(Base):
    __tablename__ = "Product"
    product_id = Column(Integer, primary_key=True)
    product_name = Column(String)
    img_resource = Column(String)
    description = Column(String)
    price = Column(Float)
    rate = Column(Float)
    category_id = Column(Integer, ForeignKey("Category.category_id"))

class Order(Base):
    __tablename__ = "Order"
    order_id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey("User.user_id"))
    order_date = Column(String)
    total_price = Column(Float)

class OrderItem(Base):
    __tablename__ = "OrderItem"
    order_item_id = Column(Integer, primary_key=True)
    order_id = Column(Integer, ForeignKey("Order.order_id"))
    product_id = Column(Integer, ForeignKey("Product.product_id"))
    amount = Column(Integer)

class Review(Base):
    __tablename__ = "Review"
    review_id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey("User.user_id"))
    product_id = Column(Integer, ForeignKey("Product.product_id"))
    rate = Column(Integer)
    comment = Column(String)

class Cart(Base):
    __tablename__ = "Cart"
    cart_id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey("User.user_id"))

class CartItem(Base):
    __tablename__ = "CartItem"
    cart_item_id = Column(Integer, primary_key=True)
    cart_id = Column(Integer, ForeignKey("Cart.cart_id"))
    product_id = Column(Integer, ForeignKey("Product.product_id"))
    amount = Column(Integer)
    
class Favorite(Base):
    __tablename__ = "Favorite"
    fav_id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey("User.user_id"))

class FavoriteItem(Base):
    __tablename__ = "FavoriteItem"
    fav_item_id = Column(Integer, primary_key=True)
    fav_id = Column(Integer, ForeignKey("Cart.cart_id"))
    product_id = Column(Integer, ForeignKey("Product.product_id"))