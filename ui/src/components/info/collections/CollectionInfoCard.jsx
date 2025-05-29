import React from 'react';
import {Card, CardHeader, CardMedia, Typography} from "@mui/material";
import {getImageUrlFromPlex} from "../../../utils/imageUtils";

function CollectionInfoCard({collection, apiKey}) {
    return (
        <Card
            sx={{
                maxWidth: 150,
                bgcolor: 'infoCard.main'
            }}
        >
            <CardHeader
                title={
                    <Typography
                        noWrap
                        variant="h6"
                        component="h4"
                        color="infoCard.text"
                    >
                        {collection.name}
                    </Typography>
                }
            />
            <CardMedia
                component="img"
                image={getImageUrlFromPlex(collection.settings, apiKey)}
                alt="collection-image"
                sx={{
                    objectFit: 'contain',
                    width: '100%',
                    height: '100%'
                }}
            />
        </Card>
    );
}

CollectionInfoCard.propTypes = {
//     collection
}

export default CollectionInfoCard;
