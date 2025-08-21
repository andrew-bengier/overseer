import React from 'react';
import PropTypes from "prop-types";
import {Button, Stack} from "@mui/material";
import {Field, Form, Formik} from "formik";
import {useIntl} from "react-intl";
import * as Yup from "yup"
import {useDispatch} from "react-redux";
import {plexLogin} from "../../../services/MediaServerConnectionService";

//validation schema
let validationSchema = Yup.object().shape({
    name: Yup.string().required("Required"),
    url: Yup.string().required("Required"),
});

function PlexConnectionForm({data, handleSave}) {
    const dispatch = useDispatch();
    const {formatMessage} = useIntl();

    const handleTest = async (values) => {
        console.log('Testing Values:', values);
        // dispatch(updatePlexAuth(values));
        await plexLogin(values);
    }

    const enableTokenButton = (values) => {
        return values.name != null && values.url != null;
    }

    const checkFormValidity = (values) => {
        return values.name != null && values.url != null && values.key != null;
    }

    const onSubmit = (values) => {
        console.log('Saving Values');
        handleSave(values);
    }

    return (
        <Formik
            initialValues={data}
            onSubmit={onSubmit}
            validationSchema={validationSchema}
        >
            {({dirty, isValid, values, handleChange, handleBlur}) => {
                return (
                    <Form>
                        <Stack
                            spacing={1}
                            direction="column"
                            justifyContent="space-between"
                            useFlexGap
                            sx={{flexWrap: 'wrap'}}
                        >
                            <Field
                                label={formatMessage({id: 'src.components.forms.connection.connectionNameFormFieldLabel'})}
                                variant="outlined"
                                fullWidth
                                name="name"
                                value={values.name}
                            />
                            <Field
                                label={formatMessage({id: 'src.components.forms.connection.connectionUrlFormFieldLabel'})}
                                variant="outlined"
                                fullWidth
                                name="url"
                                value={values.url}
                            />
                            {values.key &&
                                <Field
                                    label={formatMessage({id: 'src.components.forms.connection.connectionApiKeyFormFieldLabel'})}
                                    variant="outlined"
                                    disabled
                                    fullWidth
                                    name="key"
                                    value={values.key}
                                />
                            }
                            <Button
                                onClick={() => handleTest(values)}
                                variant="contained"
                                color="primary"
                                disabled={!enableTokenButton(values)}
                                sx={{width: '80%'}}
                            >
                                {!values.key ? 'Get Plex Token' : 'Refresh Plex Token'}
                            </Button>
                        </Stack>
                        <Button
                            disabled={checkFormValidity(values)}
                            variant="contained"
                            color="primary"
                            type="Submit"
                        >
                            Save
                        </Button>
                    </Form>
                )
            }}
        </Formik>
    )
}

PlexConnectionForm.propTypes = {
    data: PropTypes.object.isRequired
}

export default PlexConnectionForm;