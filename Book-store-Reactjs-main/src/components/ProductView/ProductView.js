import React, { useState, useEffect } from "react";
import { Container, Grid, Button, Typography } from "@material-ui/core";
import { Link } from "react-router-dom";
// Assurez-vous que commerce est correctement importé depuis le chemin approprié
import { commerce } from "../../lib/commerce";
import "./style.css";

const createMarkup = (text) => {
  return { __html: text };
};

const ProductView = () => {
  const [product, setProduct] = useState({});

  const fetchProduct = async (id) => {
    try {
      const response = await fetch(`http://localhost:8087/api/books/${id}`);
      if (!response.ok) {
        throw new Error('Failed to fetch product');
      }
      const data = await response.json();
      const { name, description, price, quantity, edition, isbn, language, image, rete, discount, date } = data;
      setProduct({
        name,
        description,
        edition,
        isbn,
        language,
        image,
        rete,
        discount,
        date,
        quantity,
        price,
      });
    } catch (error) {
      console.error('Error fetching product:', error);
    }
  };

  useEffect(() => {
    const id = window.location.pathname.split("/");
    fetchProduct(id[2]);
  }, []);

  return (
      <Container className="product-view">
        <Grid container>
          <Grid item xs={12} md={6} className="image-wrapper">
            <img src={product.image} alt={product.name} width="100px"/>
          </Grid>
          <Grid item xs={12} md={5} className="text">
            <Typography variant="h2">
              <b>{product.name}</b>
            </Typography>
            <Typography
                variant="p"
                dangerouslySetInnerHTML={createMarkup(product.description)}
            />
            <Typography variant="h3" color="secondary">
              Language: <b> {product.language} </b>
            </Typography>
            <Typography variant="h3" color="secondary">
              Edition: <b> {product.edition} </b>
            </Typography>
            <Typography variant="h3" color="secondary">
              Discount: <b> {product.discount} </b>
            </Typography>
            <Typography variant="h3" color="secondary">
              Price: <b> {product.price} </b>
            </Typography>
            <br />
            <Grid container spacing={0}>
              <Grid item xs={12}>
                <Button
                    size="large"
                    className="custom-button"
                    component={Link}
                    to="/"
                >
                  Continue Shopping
                </Button>
              </Grid>
            </Grid>
          </Grid>
        </Grid>
      </Container>
  );
};
export default ProductView;
