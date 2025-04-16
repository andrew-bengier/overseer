import React from "react";
import {Box, FormControl, FormHelperText, InputLabel, MenuItem, Select, Stack, Typography} from "@mui/material";

function SplitSelectFormField({
                                  id,
                                  label,
                                  value,
                                  options,
                                  helperText,
                                  disabled,
                                  fullwidth,
                                  externalLabel,
                                  handleChange
                              }) {
    const [selected, setSelected] = React.useState("");
    const [selectOptions, setSelectOptions] = React.useState([""]);

    React.useEffect(() => {
        setSelectOptions(options);
        setSelected(value);
    }, [value, options]);

    function handleInputChange(event) {
        setSelected(event.target.value);
        handleChange(id, event.target.value)
    }

    function SelectorWithLabel({label, selected, selectOptions}) {
        return (
            <React.Fragment>
                <InputLabel id={`${id}-label`} sx={{paddingTop: '7px'}}>{label}</InputLabel>
                <Select
                    labelId={`${id}-label`}
                    id={id}
                    value={selected}
                    label={label}
                    onChange={handleInputChange}
                    variant="outlined"
                    sx={{
                        ...(externalLabel && {
                            width: '60%',
                            paddingLeft: '10px',
                            display: 'flex',
                            flewDirection: 'row',
                            alignItems: 'center'
                        })
                    }}
                >
                    {selectOptions.map((option) => (
                        <MenuItem
                            id={option}
                            value={option}
                        >
                            <Box display="flex" justifyContent="space-between" width="100%">
                                <Typography align="left">{option.display}</Typography>
                                <Typography align="right">{option.value}</Typography>
                            </Box>
                        </MenuItem>
                    ))}
                </Select>
            </React.Fragment>
        );
    }

    function SelectorWithoutLabel({selected, selectOptions}) {
        return (
            <Select
                id={id}
                value={selected}
                onChange={handleInputChange}
                variant="outlined"
                sx={{
                    ...(externalLabel && {
                        width: '60%',
                        paddingLeft: '10px',
                        display: 'flex',
                        flewDirection: 'row',
                        alignItems: 'center'
                    })
                }}
            >
                {selectOptions.map((option) => (
                    <MenuItem
                        id={option}
                        value={option}
                    >
                        <Box display="flex" justifyContent="space-between" width="100%">
                            <Typography align="left">{option.display}</Typography>
                            <Typography align="right">{option.value}</Typography>
                        </Box>
                    </MenuItem>
                ))}
            </Select>
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
                    <SelectorWithoutLabel selected={selected} selectOptions={selectOptions}/>
                </Stack>
            ) : (
                <SelectorWithLabel label={label} selected={selected} selectOptions={selectOptions}/>
            )}
            {
                helperText && (
                    <Box display="flex" justifyContent="flex-end">
                        <FormHelperText>{helperText}</FormHelperText>
                    </Box>
                )
            }
        </FormControl>
    )
        ;
}

SplitSelectFormField.propTypes = {}

export default SplitSelectFormField;
