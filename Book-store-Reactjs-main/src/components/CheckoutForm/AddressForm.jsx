import React from 'react';
import { useForm } from 'react-hook-form';
import { Grid, TextField, Button } from '@material-ui/core';

const AddressForm = ({ onSubmit }) => {
  const { register, handleSubmit, errors } = useForm();

  const onSubmitForm = (data) => {
    onSubmit(data); // Passer les données du formulaire à la fonction de soumission fournie par le composant parent
  };

  return (
      <form onSubmit={handleSubmit(onSubmitForm)}>
        <Grid container spacing={3}>
          <Grid item xs={12} sm={6}>
            <TextField
                inputRef={register({ required: true })}
                name="firstName"
                label="First name"
                fullWidth
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
                inputRef={register({ required: true })}
                name="lastName"
                label="Last name"
                fullWidth
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
                inputRef={register({ required: true })}
                name="address1"
                label="Address"
                fullWidth
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
                inputRef={register({ required: true })}
                name="city"
                label="City"
                fullWidth
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
                inputRef={register({ required: true })}
                name="zip"
                label="ZIP / Postal code"
                fullWidth
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
                inputRef={register({ required: true })}
                name="email"
                label="Email"
                fullWidth
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
                inputRef={register({ required: true })}
                name="payment"
                label="Payment Method"
                fullWidth
            />
          </Grid>
        </Grid>
        <br />
        <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
          <Button variant="contained" color="primary" type="submit">
            Validate
          </Button>
        </div>
      </form>
  );
};

export default AddressForm;
