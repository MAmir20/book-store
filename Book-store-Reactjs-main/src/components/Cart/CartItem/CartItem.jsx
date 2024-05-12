import React, { useState, useEffect } from 'react';
import { Typography, Button, Card, CardContent, CardMedia } from '@material-ui/core';
import useStyles from './styles';

const CartItem = ({ item, onUpdateCartQty, onRemoveFromCart }) => {
    const classes = useStyles();
    const [bookDetails, setBookDetails] = useState(null);

    useEffect(() => {
        const fetchBookDetails = async () => {
            if (!item.cartLineItemsListDto[0].bookId) {
                console.error('Invalid book ID:', item.cartLineItemsListDto[0].bookId);
                return;
            }

            try {
                const response = await fetch(`http://localhost:8087/api/books/${item.cartLineItemsListDto[0].bookId}`);
                if (!response.ok) {
                    throw new Error('Failed to fetch book details');
                }
                const bookData = await response.json();
                setBookDetails(bookData);
            } catch (error) {
                console.error('Error fetching book details:', error);
            }
        };

        fetchBookDetails();
    }, [item.cartLineItemsListDto[0].bookId]);

    const handleUpdateCartQty = (cartId, productId, quantity) => {
        onUpdateCartQty(cartId, productId, quantity);
    };

    const handleRemoveFromCart = (cartId, productId) => {
        onRemoveFromCart(cartId, productId);
    };

    return (
        <Card className={classes.root}>
            {bookDetails ? (
                <>
                    <CardMedia className={classes.media} image={bookDetails.image} title={bookDetails.name} />
                    <CardContent className={classes.cardContent}>
                        <Typography variant="h6">{bookDetails.name}</Typography>
                        <Typography variant="body2">Price: ${bookDetails.price}</Typography>
                        <div className={classes.buttons}>
                            <Button type="button" size="small" onClick={() => handleUpdateCartQty(item.id, item.cartLineItemsListDto[0].bookId, item.cartLineItemsListDto[0].quantity - 1,item.cartLineItemsListDto[0].price)}>-</Button>
                            <Typography>{item.quantity}</Typography>
                            <Button type="button" size="small" onClick={() => handleUpdateCartQty(item.id, item.cartLineItemsListDto[0].bookId, item.cartLineItemsListDto[0].quantity +1 ,item.cartLineItemsListDto[0].price)}>+</Button>
                        </div>
                        <Button variant="contained" type="button" color="secondary" onClick={() => handleRemoveFromCart(item.id, item.cartLineItemsListDto[0].bookId)}>Remove</Button>
                    </CardContent>
                </>
            ) : (
                <CardContent>
                    <Typography variant="body2">Loading book details...</Typography>
                </CardContent>
            )}
        </Card>
    );
};

export default CartItem;
