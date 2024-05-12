import React, { useState, useEffect } from "react";
import { Grid } from "@material-ui/core";
import Product from "../Products/Product/Product.js";
import useStyles from "../Products/styles.js"; // Assurez-vous que le chemin d'importation est correct
import "react-responsive-carousel/lib/styles/carousel.min.css";
import "../ProductView/style.css";

const CategoryPage = ({ category, onAddToCart }) => {
    const classes = useStyles(); // Utilisation des classes définies dans le fichier de styles
    const [products, setProducts] = useState([]);
    useEffect(() => {
        const fetchCategoryProducts = async () => {
            try {
                const response = await fetch(`http://localhost:8087/api/books/category/${category.id}`); // Utilisation de category.id
                if (!response.ok) {
                    throw new Error(`Failed to fetch ${category.name} products`);
                }
                const data = await response.json();
                setProducts(data);
            } catch (error) {
                console.error(`Error fetching ${category.name} products:`, error);
            }
        };

        fetchCategoryProducts();
    }, [category]);

    return (
        <main className={classes.mainPage}>
            <div className={classes.toolbar} />
            <div className={classes.categorySection}>
                <h3 className={classes.categoryHeader}>
                    <span style={{ color: "#f1361d" }}>{category.name}&nbsp;</span>Books
                </h3>
                <h3 className={classes.categoryDesc}>
                    Browse our {category.name} Collection
                </h3>
                <Grid className={classes.categoryFeatured} container justify="center" spacing={3}>
                    {products.map((product) => (
                        <Grid className={classes.categoryFeatured} item xs={8} sm={5} md={3} lg={2} key={product.id} id="pro">
                            <Product product={product} onAddToCart={onAddToCart} />
                        </Grid>
                    ))}
                </Grid>
            </div>
        </main>
    );
};

export default CategoryPage;
