import React from 'react';
import "react-big-calendar/lib/css/react-big-calendar.css";
import './CalendarView.css';
import {Calendar, momentLocalizer} from "react-big-calendar";
import moment from "moment";
import {Box} from "@mui/material";
import {useSelector} from "react-redux";

export default function CalendarView() {
    const theme = useSelector((state) => state.theme.theme);
    const dayPropGetter = (date) => {
        const today = new Date();

        if (
            // Current date
            date.getDate() === today.getDate() &&
            date.getMonth() === today.getMonth() &&
            date.getFullYear() === today.getFullYear()
        ) {
            return {
                style: {
                    backgroundColor: theme.palette.primary.light,
                },
            };
            // } else if (date.getMonth() !== today.getMonth()) {
            //     return {
            //         style: {
            //             backgroundColor: theme.palette.primary.main,
            //         },
            //     };
        } else {
            return null;
        }
        // const today = new Date();
        // if (
        //     date.getDate() === today.getDate() &&
        //     date.getMonth() === today.getMonth() &&
        //     date.getFullYear() === today.getFullYear()
        // ) {
        return {
            style: {
                backgroundColor: 'purple', // Or any color you prefer
            },
        };
        // }
        // return {}; // Return an empty object for other days
    };

    const events = [
        // {
        //     start: moment().toDate(),
        //     end: moment()
        //         .add(1, "days")
        //         .toDate(),
        //     title: "Some title"
        // },
        // {
        //     id: 2,
        //     title: "DTS STARTS",
        //     start: moment().toDate(),
        //     end: moment().toDate(),
        // },
    ];

    return (
        <Box
            sx={{
                display: 'flex',
                flexDirection: 'column',
                width: '100%'
                // paddingTop: '200px'
            }}
        >
            <Calendar
                localizer={momentLocalizer(moment)}
                defaultDate={new Date()}
                defaultView="month"
                events={events}
                style={{height: '100vh'}}
                // dayPropGetter={dayPropGetter}
            />
        </Box>
    );
}