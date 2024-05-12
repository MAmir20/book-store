import React, { useState, useEffect } from "react";
import { useHistory } from "react-router-dom";
import { CssBaseline } from "@material-ui/core";
import { commerce } from "./lib/commerce";
import Products from "./components/Products/Products";
import Navbar from "./components/Navbar/Navbar";
import Cart from "./components/Cart/Cart";
import Checkout from "./components/CheckoutForm/Checkout/Checkout";
import Confirmation from "./components/CheckoutForm/Checkout/Confirmation";
import ProductView from "./components/ProductView/ProductView";
import Manga from "./components/Manga/Manga";
import Footer from "./components/Footer/Footer";
import { BrowserRouter as Router, Switch, Route,redirect } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "mdbreact/dist/css/mdb.css";
import "@fortawesome/fontawesome-free/css/all.min.css";
import loadingImg from "./assets/loader.gif";
import "./style.css";
import Fiction from "./components/Fiction/Fiction";
import Biography from "./components/Bio/Biography";
import Login from "./components/Login/Login";
import SignUp from "./components/Login/SignUp";
import CategoryPage from "./components/CategoryPage/CategoryPage";

const App = () => {
  const [mobileOpen, setMobileOpen] = React.useState(false);
  const [products, setProducts] = useState([]);
  const [mangaProducts, setMangaProducts] = useState([]);
  const [fictionProducts, setFictionProducts] = useState([]);
  const [bioProducts, setBioProducts] = useState([]);
  const [featureProducts, setFeatureProducts] = useState([]);
  const [cart, setCart] = useState({});
  const [cartfinal, setCartfinal] = useState({});
  const [order, setOrder] = useState({});
  const [errorMessage, setErrorMessage] = useState("");
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userId, setUserId] = useState(null);
  const history = useHistory();
  const [categories, setCategories] = useState([]);
  //useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await fetch("http://localhost:8087/api/categories");
        if (!response.ok) {
          throw new Error("Failed to fetch categories");
        }
        const data = await response.json();
        console.log(data); // Vérifiez les données de catégorie récupérées
        setCategories(data);
      } catch (error) {
        console.error("Error fetching categories:", error);
      }
    };



  //fetchCategories();
  //}, []);

  useEffect(() => {
    console.log("History:", history);
  }, [history]);


  const fetchProducts = async () => {
    try {
      const response = await fetch("http://localhost:8087/api/books");
      if (!response.ok) {
        throw new Error("Failed to fetch products");
      }
      const data = await response.json();
      setProducts(data);
    } catch (error) {
      console.error("Error fetching products:", error);
    }
  };

  const fetchMangaProducts = async () => {
    const { data } = await commerce.products.list({
      category_slug: ["manga"],
    });

    setMangaProducts(data);
  };



  const fetchFictionProducts = async () => {
    const { data } = await commerce.products.list({
      category_slug: ["fiction"],
    });

    setFictionProducts(data);
  };

  const fetchBioProducts = async () => {
    const { data } = await commerce.products.list({
      category_slug: ["biography"],
    });

    setBioProducts(data);
  };

  const fetchCart = async () => {
    try {
      const response = await fetch("http://localhost:8087/api/carts");
      if (!response.ok) {
        throw new Error("Failed to fetch cart data");
      }
      const data = await response.json();
      if (data && typeof data === 'object') {
        setCart(data); // Traiter les données comme un objet
        const carts = await response.json();
        const orderedCarts = carts.filter(cart => cart.status === 0);
        const sortedCarts = orderedCarts.sort((a, b) => new Date(b.date) - new Date(a.date));
        const latestCart = sortedCarts[0];
        setCartfinal(latestCart);
      } else {
        throw new Error("Invalid cart data format");
      }
    } catch (error) {
      console.error("Error fetching cart data:", error);
    }
  };


  const handleLogin = (userId) => {
    setUserId(userId);
    console.log(userId);
    setIsLoggedIn(true);
  };
  const handleLogout = () => {
    setIsLoggedIn(false);
  };


  const handleAddToCart = async (productId, quantity, userId, price) => {
    console.log(history);
    if (!isLoggedIn) {
      history.push("/login");
    } else {
      try {
          console.log(userId);
          const response = await fetch("http://localhost:8087/api/carts", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({
              userId: 1,
              date: new Date().toISOString(),
              total :price*quantity,
              cartLineItemsDtoList: [
                {
                  bookId: productId,
                  quantity: quantity,
                  price: price,
                },
              ],
              status: 0, // Ajout du statut pour créer un nouveau panier
            }),
          });
          if (!response.ok) {
            throw new Error("Failed to add product to cart");
          }
          console.log("Product added to cart successfully");
      } catch (error) {
        console.error("Error updating cart:", error);
      }
    }
  };



  const handleUpdateCartQty = async (cartId, productId, quantity, userId,price) => {
    try {
      if (!cartId || !productId || quantity < 0) {
        console.error('Invalid data provided for updating cart.');
        throw new Error('Invalid data provided');
      }
      console.log('Updating cart with the following data:', { cartId, productId, quantity });

      // Log the JSON body of the request
      const requestBody = JSON.stringify({
        userId: userId,
        date: new Date().toISOString(),
        status: 0,
        cartLineItemsDtoList: [
          {
            bookId: productId,
            quantity: quantity,
            price :price,
          },
        ],
      });
      console.log('Request body:', requestBody);

      const response = await fetch(`http://localhost:8087/api/carts/${cartId}`, {
        method: 'PUT',
        headers: {
          "Content-Type": "application/json",
        },
        body: requestBody, // Utilize the requestBody variable
      });

      if (!response.ok) {
        throw new Error('Failed to update cart. Status:' + response.status);
      }

      const updatedCart = await response.json();

      console.log('Cart updated successfully');
      return updatedCart;
    } catch (error) {
      console.error('Error updating cart:', error);
      throw error;
    }
  };


  const handleRemoveFromCart = async (cartId, bookId) => {
    try {
      const response = await fetch(`http://localhost:8087/api/carts/${cartId}`, {
        method: 'DELETE',
      });

      if (response.ok) {
        console.log(`CartItem with bookId ${bookId} removed successfully`);

        refreshCart();
      } else {
        console.error('Failed to remove cart item');
      }
    } catch (error) {
      console.error('Error removing cart item:', error);
    }
  };


  const handleEmptyCart = async () => {
    try {
      const response = await fetch(`http://localhost:8087/api/carts`);
      if (!response.ok) {
        throw new Error(`Failed to fetch carts`);
      }
      const allCarts = await response.json();
      const userCarts = allCarts.filter(cart => cart.userId === userId);
      const cartIds = userCarts.map(cart => cart.id);
      await Promise.all(cartIds.map(async (cartId) => {
        const deleteResponse = await fetch(`http://localhost:8087/api/carts/${cartId}`, {
          method: 'DELETE',
        });
        if (!deleteResponse.ok) {
          throw new Error(`Failed to delete cart with ID ${cartId}`);
        }
      }));

      console.log('All carts for user successfully emptied');
      setCart({});
    } catch (error) {
      console.error('Error emptying carts for user:', error);
    }
  };


  const refreshCart = async ( ) => {
    try {
      const response = await fetch('http://localhost:8087/api/carts/');

      if (response.ok) {
        const newCart = await response.json();
        setCart(newCart);
      } else {
        console.error('Failed to refresh cart');
      }
    } catch (error) {
      console.error('Error refreshing cart:', error);
    }
  };

  const handleCaptureCheckout = async () => {
    try {
      refreshCart();
    } catch (error) {
      setErrorMessage(error.data.error.message);
    }
  };
  const fetchOrder = async () => {
    try {
      const response = await fetch('http://localhost:8087/api/orders', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error('Failed to fetch order');
      }

      const orderData = await response.json();
      setOrder(orderData);
    } catch (error) {
      console.error('Error fetching order:', error);
    }
  };
  useEffect(() => {
    fetchCategories();
    fetchProducts();
    //fetchFeatureProducts();
    fetchCart();
    //fetchMangaProducts();
    //fetchFictionProducts();
    //fetchBioProducts();
    fetchOrder();
  }, []);

  const handleDrawerToggle = () => setMobileOpen(!mobileOpen);

  return (
      <div>
        {products.length > 0 ? (
            <>
              <Router>
                <div style={{display: "flex"}}>
                  <CssBaseline/>
                  <Navbar
                      totalItems={cart.total}
                      handleDrawerToggle={handleDrawerToggle}
                  />
                  <Switch>
                    <Route exact path="/">
                      <Products
                          products={products}
                          cart={ cart }
                          featureProducts={featureProducts}
                          onAddToCart={handleAddToCart}
                          handleUpdateCartQty={handleUpdateCartQty}
                          userId={userId}
                          categories={categories}
                      />
                    </Route>
                    <Route exact path="/cart">
                      <Cart
                          cart={cart}
                          onUpdateCartQty={handleUpdateCartQty}
                          onRemoveFromCart={handleRemoveFromCart}
                          onEmptyCart={handleEmptyCart}
                          userId={userId}
                      />
                    </Route>
                    <Route path="/checkout" exact>
                      <Checkout
                          cart={cart}
                          order={order}
                          onCaptureCheckout={handleCaptureCheckout}
                          error={errorMessage}
                      />
                    </Route>
                    <Route path="/product-view/:id" exact>
                      <ProductView />
                    </Route>
                    <Route path="/manga" exact>
                      <Manga
                          mangaProducts={mangaProducts}
                          onAddToCart={handleAddToCart}
                          handleUpdateCartQty={handleUpdateCartQty}
                      />
                    </Route>
                    <Route path="/fiction" exact>
                      <Fiction
                          fictionProducts={fictionProducts}
                          onAddToCart={handleAddToCart}
                          handleUpdateCartQty={handleUpdateCartQty}
                      />
                    </Route>
                    <Route path="/biography" exact>
                      <Biography
                          bioProducts={bioProducts}
                          onAddToCart={handleAddToCart}
                          handleUpdateCartQty={handleUpdateCartQty}
                      />
                    </Route>
                    <Route path="/confirmation" exact>
                      <Confirmation />
                    </Route>
                    <Route path="/login" exact>
                      <Login handleLogin={handleLogin} setIsLoggedIn={setIsLoggedIn} />
                    </Route>

                    <Route path="/signup" exact>
                      <SignUp />
                    </Route>
                    {categories.map((category) => (
                        <Route key={category.id} path={`/${category.id}`} exact>
                          <CategoryPage
                              category={category}
                              onAddToCart={handleAddToCart}
                              handleUpdateCartQty={handleUpdateCartQty}
                          />
                        </Route>

                    ))}
                  </Switch>
                </div>
              </Router>
              <Footer />
            </>
        ) : (
            <div className="loader">
              <img src={loadingImg} alt="Loading" />
            </div>
        )}
      </div>
  );
};

export default App;