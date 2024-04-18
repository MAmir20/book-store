import React, { useState, useEffect, useRef } from "react";
import { Grid, InputAdornment, Input } from "@material-ui/core";
import SearchIcon from "@material-ui/icons/Search";
import Product from "./Product/Product.js";
import useStyles from "./styles";
import logo1 from "../../assets/Bookshop.gif";
import scrollImg from "../../assets/scroll.gif";
import "../ProductView/style.css";
import { Link } from "react-router-dom";
import "react-responsive-carousel/lib/styles/carousel.min.css";
import { Carousel } from "react-responsive-carousel";

const Products = ({ products, onAddToCart, featureProducts, categories ,cart}) => {
  const classes = useStyles();
  const [searchTerm, setSearchTerm] = useState("");
  const sectionRef = useRef(null);

  useEffect(() => {
    // Scroll to the section when the component mounts
    if (sectionRef.current) {
      sectionRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, []);

  return (
      <main className={classes.mainPage}>
        <div className={classes.toolbar} />
        <img src={scrollImg} className={classes.scrollImg} />
        <div className={classes.hero}>
          <img className={classes.heroImg} src={logo1} height="720px" />

          <div className={classes.heroCont}>
            <h1 className={classes.heroHeader}>
              Discover Your Next Favorite Book Here.
            </h1>
            <h3 className={classes.heroDesc} ref={sectionRef}>
              Explore our curated collection of new and popular books to find your
              next literary adventure.
            </h3>
            <div className={classes.searchs}>
              <Input
                  className={classes.searchb}
                  type="text"
                  placeholder="Which book are you looking for?"
                  onClick={() => sectionRef.current.scrollIntoView({ behavior: "smooth" })}
                  onChange={(event) => setSearchTerm(event.target.value)}
                  startAdornment={
                    <InputAdornment position="start">
                      <SearchIcon />
                    </InputAdornment>
                  }
              />
            </div>
          </div>
        </div>

        {categories && categories.length > 0 && (
            <div className={classes.categorySection}>
              <h1 className={classes.categoryHeader}>Categories</h1>
              <h3 className={classes.categoryDesc}>
                Browse our featured categories
              </h3>
              <div className={classes.buttonSection}>
                {categories.map((category) => (
                    <div key={category.id}>
                      <Link to={`/${category.id}`}>
                        <button
                            className={classes.categoryButton}
                            style={{ backgroundImage: `url(${category.image})` }}
                        ></button>
                      </Link>
                      <div className={classes.categoryName}>{category.name}</div>
                    </div>
                ))}
              </div>
            </div>
        )}

        <div className={classes.carouselSection}>
          <Carousel
              showIndicators={false}
              autoPlay={true}
              infiniteLoop={true}
              showArrows={true}
              showStatus={false}
          >
            {/* Affichage des catégories directement dans le carousel */}
            {categories.map((category) => (
                <div key={category.id}>
                  <Link to={category.slug}>
                    <button
                        className={classes.categoryButton}
                        style={{ backgroundImage: `url(${category.image})` }}
                    ></button>
                  </Link>
                  <div className={classes.categoryName}>{category.name}</div>
                </div>
            ))}
          </Carousel>
        </div>

        {searchTerm === "" && (
            <>

            </>
        )}

        <div>
          {searchTerm === "" && (
              <>
                <h1 className={classes.booksHeader}>
                  Discover <span style={{ color: "#f1361d" }}>Books</span>
                </h1>
                <h3 className={classes.booksDesc}>
                  Explore our comprehensive collection of books.
                </h3>
              </>
          )}
          <div className={classes.mobileSearch}>
            <div className={classes.mobSearchs}>
              <Input
                  className={classes.mobSearchb}
                  type="text"
                  placeholder="Search for books"
                  onChange={(event) => setSearchTerm(event.target.value)}
                  startAdornment={
                    <InputAdornment position="start">
                      <SearchIcon />
                    </InputAdornment>
                  }
              />
            </div>
          </div>
          <Grid
              className={classes.content}
              container
              justify="center"
              spacing={2}
          >
            {products &&
                products
                    .filter((product) => {
                      if (searchTerm === "") {
                        return product;
                      } else if (
                          product.name
                              .toLowerCase()
                              .includes(searchTerm.toLowerCase())
                      ) {
                        return product;
                      }
                    })
                    .map((product) => (
                        <Grid
                            className={classes.content}
                            item
                            xs={6}
                            sm={6}
                            md={4}
                            lg={3}
                            key={product.id}
                        >
                          <Product product={product} onAddToCart={onAddToCart} />
                        </Grid>
                    ))}
          </Grid>
        </div>
      </main>
  );
};

export default Products;
