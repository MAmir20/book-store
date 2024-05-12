import React, { useEffect } from 'react';
import { Container, Typography, Button, Grid } from '@material-ui/core';
import { Link } from 'react-router-dom';

import CartItem from './CartItem/CartItem';
import useStyles from './styles';

const Cart = ({ cart, onUpdateCartQty, onRemoveFromCart, onEmptyCart }) => {
    const classes = useStyles();

    useEffect(() => {
        console.log(" cart :", cart);
    }, [cart]);

    const handleEmptyCart = () => onEmptyCart(1);

    const renderEmptyCart = () => (
        <Typography variant="subtitle1">
            You have no items in your shopping cart,
            <Link className={classes.link} to="/"> start adding some</Link>!
        </Typography>
    );

    const filteredCart = cart;

    const renderCart = () => (
        <>
            <Grid container spacing={4}>
                {filteredCart.map((cartItem) => (
                    <Grid item xs={12} sm={4} key={cartItem.id}>
                        <CartItem item={cartItem} onUpdateCartQty={onUpdateCartQty} onRemoveFromCart={onRemoveFromCart} />
                    </Grid>
                ))}
            </Grid>
            <div className={classes.cardDetails}>
                <Typography variant="h5">Subtotal: <b>{filteredCart[0]?.total}</b></Typography>
                <div>
                    <Button className={classes.emptyButton} size="large" type="button" variant="contained" color="secondary" onClick={handleEmptyCart}>Empty cart</Button>
                    <Button className={classes.checkoutButton} component={Link} to="/checkout" size="large" type="button" variant="contained">Checkout</Button>
                </div>
            </div>
        </>
    );


    return (
        <Container>
            <div className={classes.toolbar} />
            <Typography className={classes.title} variant="h5" gutterBottom><b>Your Shopping Cart</b></Typography>
            <hr/>
            {filteredCart.length > 0 ? renderCart() : renderEmptyCart()}
        </Container>
    );
};

export default Cart;