import React from 'react';
import {Paper, Table, TableBody, TableCell, tableCellClasses, TableContainer, TableHead, TableRow} from "@mui/material";
import PropTypes from "prop-types";

function InfoTable({columns, data, tableLabel}) {
    return (
        <Paper
            elevation={0}
            sx={{
                width: '100%',
                padding: '10px',
                marginTop: '10px',
                overflow: 'hidden'
            }}
        >
            <TableContainer>
                <Table
                    stickyHeader
                    aria-label={tableLabel}
                    sx={{
                        [`& .${tableCellClasses.body}`]: {
                            borderBottom: "none"
                        }
                    }}
                >
                    <TableHead>
                        <TableRow
                            sx={{
                                borderBottom: "thick"
                            }}
                        >
                            {columns.map((column) => (
                                <TableCell
                                    key={column.id}
                                    align={column.align}
                                    style={{
                                        minWidth: column.minWidth
                                    }}
                                >
                                    {column.label}
                                </TableCell>
                            ))}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {data.map((row) => {
                            return (
                                <TableRow key={row.fileName}>
                                    {columns.map((column) => {
                                        const value = row[column.id];
                                        return (
                                            <TableCell key={column.id} align={column.align}>
                                                {column.format ?
                                                    column.function ?
                                                        column.functionType === 'selection' ?
                                                            column.format(row)
                                                            : column.format(row)
                                                        : column.format(value)
                                                    : value}
                                            </TableCell>
                                        );
                                    })}
                                </TableRow>
                            );
                        })}
                    </TableBody>
                </Table>
            </TableContainer>
        </Paper>
    );
}

InfoTable.propTypes = {
    columns: PropTypes.shape({
        id: PropTypes.string.isRequired,
        label: PropTypes.string.isRequired,
        minWidth: PropTypes.number.isRequired,
        align: PropTypes.oneOf(['left', 'center', 'right']).isRequired,
        format: PropTypes.func
    }).isRequired,
    data: PropTypes.array.isRequired,
    tableLabel: PropTypes.string.isRequired
}

export default InfoTable;
