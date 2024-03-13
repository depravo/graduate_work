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

engine = create_engine("sqlite:///online_shop.db", echo=True)
Base = declarative_base()


class Product(Base):
    __tablename__ = "Product"
    product_id = Column(Integer, primary_key=True)
    product_name = Column(String)
    img_resource = Column(String)
    description = Column(String)
    price = Column(Float)
    rate = Column(Float)
    category_id = Column(Integer)


class Category(Base):
    __tablename__ = "Category"
    category_id = Column(Integer, primary_key=True)
    category_name = Column(String)
