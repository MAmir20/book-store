import React from 'react';
import { TextField, Grid } from '@material-ui/core';
import { Controller } from 'react-hook-form';

function FormInput({ name, label, required, control }) {
    return (
        <Grid item xs={12} sm={6}>
            <Controller
                as={TextField}
                name={name}
                control={control}
                label={label}
                fullWidth
                required={required}
            />
        </Grid>
    );
}

export default FormInput;
