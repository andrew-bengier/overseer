import React from "react";
import {FormControl, FormHelperText, InputLabel, Stack, TextField, Typography} from "@mui/material";

function TextFormField({
                           id,
                           label,
                           value,
                           helperText,
                           disabled,
                           fullwidth,
                           externalLabel,
                           handleChange
                       }) {
    const [displayValue, setDisplayValue] = React.useState("");

    React.useEffect(() => {
        setDisplayValue(value);
    }, [value]);


    function handleInputChange(event) {
        event.preventDefault();
        setDisplayValue(event.target.value);
        handleChange(id, event.target.value)
    }

    function TextWithLabel({label, displayValue}) {
        return (
            <React.Fragment>
                <InputLabel id={`${id}-label`} sx={{paddingTop: '7px'}}>{label}</InputLabel>
                <TextField
                    labelId={label ? `${id}-label` : null}
                    id={id}
                    value={displayValue}
                    label={label ? label : null}
                    onChange={handleInputChange}
                    variant="outlined"
                    sx={{
                        ...(externalLabel && {
                            width: '60%',
                            paddingLeft: '10px'
                        })
                    }}
                />
            </React.Fragment>
        );
    }

    function TextWithoutLabel({displayValue}) {
        return (
            <TextField
                id={id}
                value={displayValue}
                onChange={handleInputChange}
                variant="outlined"
                sx={{
                    ...(externalLabel && {
                        width: '60%',
                        paddingLeft: '10px'
                    })
                }}
            />
        );
    }

    return (
        <FormControl
            fullWidth={fullwidth}
            disabled={disabled}
            sx={{paddingY: '5px'}}
        >
            {externalLabel ? (
                <Stack
                    direction="row"
                    spacing="2"
                    sx={{
                        alignItems: 'center',
                        justifyContent: 'space-between'
                    }}
                >
                    <Typography id={`${id}-label`} sx={{paddingRight: '20px'}}>{label}</Typography>
                    <TextWithoutLabel displayValue={displayValue}/>
                </Stack>
            ) : (
                <TextWithLabel label={label} displayValue={displayValue}/>
            )}
            {helperText && <FormHelperText>{helperText}</FormHelperText>}
        </FormControl>
    );
}

TextFormField.propTypes = {}

export default TextFormField;
