import React from "react";
import {
    Box,
    Chip,
    FormControl,
    FormHelperText,
    InputLabel,
    MenuItem,
    OutlinedInput,
    Select,
    Stack,
    Typography
} from "@mui/material";

function SelectFormField({
                             id,
                             label,
                             value,
                             options,
                             multiple,
                             helperText,
                             disabled,
                             fullwidth,
                             externalLabel,
                             handleChange
                         }) {
    const [selected, setSelected] = React.useState("");
    const [selectedList, setSelectedList] = React.useState([]);
    const [selectOptions, setSelectOptions] = React.useState([""]);

    React.useEffect(() => {
        setSelectOptions(options);

        if (multiple) {
            setSelectedList(value);
        } else {
            setSelected(value);
        }
    }, [value, options]);

    function optionObject(option, field) {
        if (option instanceof Object) {
            return option[field] ? option[field] : option;
        } else {
            return option;
        }
    }

    function handleInputChange(event) {
        event.preventDefault();
        setSelected(event.target.value);
        handleChange(id, event.target.value)
    }

    function handleMultipleInputChange(event) {
        const {target: {value}} = event;
        let currentSelected = typeof value === 'string' ? value.split(',') : value;
        setSelectedList(currentSelected);
        handleChange(id, currentSelected);
    }

    function SelectorWithLabel({label, selected, selectOptions}) {
        return (
            <React.Fragment>
                <InputLabel id={`${id}-label`} sx={{paddingTop: '7px'}}>{label}</InputLabel>
                <Select
                    labelId={label ? `${id}-label` : null}
                    id={id}
                    value={selected}
                    label={label ? label : null}
                    onChange={handleInputChange}
                    variant="outlined"
                    sx={{
                        ...(externalLabel && {
                            width: '60%',
                            paddingLeft: '10px'
                        })
                    }}
                >
                    {selectOptions.map((option) => (
                        <MenuItem
                            key={optionObject(option, 'key')}
                            value={optionObject(option, 'value')}
                        >
                            {optionObject(option, 'display')}
                        </MenuItem>
                    ))}
                </Select>
            </React.Fragment>
        );
    }

    function MultiSelectorWithLabel({label, selectedList, selectOptions}) {
        return (
            <React.Fragment>
                <InputLabel id={`${id}-label`} sx={{paddingTop: '7px'}}>{label}</InputLabel>
                <Select
                    labelId={label ? `${id}-label` : null}
                    id={id}
                    value={selectedList}
                    label={label ? label : null}
                    onChange={handleMultipleInputChange}
                    variant="outlined"
                    sx={{
                        ...(externalLabel && {
                            width: '60%',
                            paddingLeft: '10px'
                        })
                    }}
                    input={<OutlinedInput id="select-multiple-chip" label="Chip"/>}
                    renderValue={(selected) => (
                        <Box sx={{display: 'flex', flexWrap: 'wrap', gap: 0.5}}>
                            {selected.map((value) => (
                                <Chip key={optionObject(value, 'key')} label={optionObject(value, 'display')}/>
                            ))}
                        </Box>
                    )}
                    MenuProps={MenuProps}
                >
                    {selectOptions.map((option) => (
                        <MenuItem
                            key={optionObject(option, 'key')}
                            value={optionObject(option, 'value')}
                        >
                            {optionObject(option, 'display')}
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
                        paddingLeft: '10px'
                    })
                }}
            >
                {selectOptions.map((option) => (
                    <MenuItem
                        key={optionObject(option, 'key')}
                        value={optionObject(option, 'value')}
                    >
                        {optionObject(option, 'display')}
                    </MenuItem>
                ))}
            </Select>
        );
    }

    function MultiSelectorWithoutLabel({selectedList, selectOptions}) {
        return (
            <Select
                id={id}
                value={selectedList}
                onChange={handleMultipleInputChange}
                variant="outlined"
                sx={{
                    ...(externalLabel && {
                        width: '60%',
                        paddingLeft: '10px'
                    })
                }}
                input={<OutlinedInput id="select-multiple-chip" label="Chip"/>}
                renderValue={(selected) => (
                    <Box sx={{display: 'flex', flexWrap: 'wrap', gap: 0.5}}>
                        {selected.map((value) => (
                            <Chip key={optionObject(value, 'key')} label={optionObject(value, 'display')}/>
                        ))}
                    </Box>
                )}
                MenuProps={MenuProps}
            >
                {selectOptions.map((option) => (
                    <MenuItem
                        key={optionObject(option, 'key')}
                        value={optionObject(option, 'value')}
                    >
                        {optionObject(option, 'display')}
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
                    {multiple ? (
                        <MultiSelectorWithoutLabel selectedList={selectedList} selectOptions={selectOptions}/>
                    ) : (
                        <SelectorWithoutLabel selected={selected} selectOptions={selectOptions}/>
                    )}
                </Stack>
            ) : (
                multiple ? (
                    <MultiSelectorWithLabel label={label} selectedList={selectedList}
                                            selectOptions={selectOptions}/>
                ) : (
                    <SelectorWithLabel label={label} selected={selected} selectOptions={selectOptions}/>
                )
            )}
            {helperText && <FormHelperText>{helperText}</FormHelperText>}
        </FormControl>
    );
}

SelectFormField.propTypes = {}

export default SelectFormField;
