import React from 'react';
import {
    Paper,
    Table,
    TableBody,
    TableCell,
    tableCellClasses,
    TableContainer,
    TableHead,
    TablePagination,
    TableRow
} from "@mui/material";
import PropTypes from "prop-types";

function FileDownloadTable({columns, data, tableLabel, defaultPageSize = 10, handleSelection, handleDownload}) {
    const [page, setPage] = React.useState(0);
    const [rowsPerPage, setRowsPerPage] = React.useState(defaultPageSize);

    const handlePageChange = (event, pageNumber) => {
        setPage(pageNumber);
    };

    const handleRowsPerPageChange = (event) => {
        setRowsPerPage(event.target.value);
        setPage(0);
    }

    function handleFileSelection(row) {
        // console.log(row);
        handleSelection(row);
    }

    function handleFileDownload(row) {
        // console.log(row);
        handleDownload(row);
    }

    return (
        // <Container
        //     disableGutters
        //     sx={{
        //         padding: 0,
        //         width: '100%'
        //     }}
        // >
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
                        {data
                            .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                            .map((row) => {
                                return (
                                    <TableRow
                                        // hover role="checkbox" tabIndex={-1}
                                        key={row.fileName}
                                        // onClick={() => handleRowSelection(row)}
                                    >
                                        {columns.map((column) => {
                                            const value = row[column.id];
                                            return (
                                                <TableCell key={column.id} align={column.align}>
                                                    {column.format ?
                                                        column.function ?
                                                            column.functionType === 'selection' ?
                                                                column.format(row, handleFileSelection)
                                                                : column.format(row, handleFileDownload)
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
            <TablePagination
                rowsPerPageOptions={[10, 25, 100]}
                component="div"
                count={data.length}
                rowsPerPage={rowsPerPage}
                page={page}
                onPageChange={handlePageChange}
                onRowsPerPageChange={handleRowsPerPageChange}
            />
            {/*</Container>*/}
        </Paper>
    );
}

FileDownloadTable.propTypes = {
    columns: PropTypes.shape({
        id: PropTypes.string.isRequired,
        label: PropTypes.string.isRequired,
        minWidth: PropTypes.number.isRequired,
        align: PropTypes.oneOf(['left', 'center', 'right']).isRequired,
        format: PropTypes.func
    }).isRequired,
    data: PropTypes.array.isRequired,
    tableLabel: PropTypes.string.isRequired,
    defaultPageSize: PropTypes.number,
    handleSelection: PropTypes.func.isRequired,
    handleDownload: PropTypes.func.isRequired
}

export default FileDownloadTable;
