import React, { useEffect, useState } from 'react';
import { CssBaseline, Paper, Stepper, Step, StepLabel, Typography, CircularProgress, Divider, Button } from '@material-ui/core';
import { Link, useHistory } from 'react-router-dom';
import AddressForm from '../AddressForm';
import useStyles from './styles';

const steps = ['Shipping address'];

const Checkout = ({ error }) => {
    const[cart, setCart] = useState(null);
    const [order, setOrder] = useState(null);
    const [loading, setLoading] = useState(true);
    const classes = useStyles();
    const history = useHistory();

    useEffect(() => {
        fetchCart();
    }, []);

    const fetchCart = async () => {
        try {
            const response = await fetch('http://localhost:8087/api/carts');
            if (!response.ok) {
                throw new Error('Failed to fetch orders');
            }
            const carts = await response.json();
            const orderedCarts = carts.filter(cart => cart.status === 0);
            const sortedCarts = orderedCarts.sort((a, b) => new Date(b.date) - new Date(a.date));
            const latestCart = sortedCarts[0];
            setCart(latestCart);
            setLoading(false); // Mettre à jour l'état de chargement une fois que la commande est récupérée
        } catch (error) {
            console.error('Error fetching orders:', error);
            setLoading(false); // Mettre à jour l'état de chargement en cas d'erreur
        }
    };

    const createOrder = async (cart, paymentMethod) => {
        try {
            const response = await fetch('http://localhost:8087/api/orders', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    date: new Date().toISOString(),
                    status: 'ORDERED',
                    payment: paymentMethod,
                    deliveryDate: null,
                    cartId: cart.id,
                }),
            });

            if (!response.ok) {
                throw new Error('Failed to create order');
            }

            const responseData = await response.json();
            console.log('Order created:', responseData);
            setOrder(responseData); // Mettre à jour la commande avec les données renvoyées par l'API
            history.push('/confirmation');
        } catch (error) {
            console.error('Error creating order:', error);
        }
    };

    const handleFormSubmit = (formData) => {
        const paymentMethod = formData.payment;
        createOrder(cart, paymentMethod);
    };
    return (
        <>
            <CssBaseline />
            <div className={classes.toolbar} />
            <main className={classes.layout}>
                <Paper className={classes.paper}>
                    <Typography variant="h4" align="center">Checkout</Typography>
                        <AddressForm onSubmit={handleFormSubmit} />
                </Paper>
            </main>
        </>
    );
};

export default Checkout;
