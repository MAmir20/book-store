import React, { useEffect, useState } from 'react';
import { Typography, Button, CircularProgress } from '@material-ui/core';
import { Link } from 'react-router-dom';
import useStyles from './styles';

const Confirmation = () => {
    const [order, setOrder] = useState(null);
    const [loading, setLoading] = useState(true);
    const classes = useStyles();

    useEffect(() => {
        fetchOrder();
    }, []);

    const fetchOrder = async () => {
        try {
            const response = await fetch('http://localhost:8087/api/orders');
            if (!response.ok) {
                throw new Error('Failed to fetch orders');
            }
            const orders = await response.json();
            const orderedOrders = orders.filter(order => order.status === 'ORDERED');
            const sortedOrders = orderedOrders.sort((a, b) => new Date(b.date) - new Date(a.date));
            const latestOrderedOrder = sortedOrders[0];
            setOrder(latestOrderedOrder);
            setLoading(false); // Mettre à jour l'état de chargement une fois que la commande est récupérée
        } catch (error) {
            console.error('Error fetching orders:', error);
            setLoading(false); // Mettre à jour l'état de chargement en cas d'erreur
        }
    };

    return (
        <div className={classes.container}>
            {loading ? (
                <div className={classes.spinner}>
                    <CircularProgress />
                </div>
            ) : order ? (
                <div className={classes.content}>
                    <Typography variant="h5" className={classes.thankYouMessage}>Thank you for your purchase!</Typography>
                    <Button component={Link} variant="outlined" to="/" className={classes.button}>Back to home</Button>
                </div>
            ) : (
                <Typography variant="h5">Error: Failed to fetch order</Typography>
            )}
        </div>
    );
};

export default Confirmation;
