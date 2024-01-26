import React from "react";

const ArchiveComponent = ({ archives }) => {
    return (
        <>
            <div style={{ overflow: "auto" }}>
                <div class="table-responsive">
                    <table
                        id="archive"
                        className="table table-striped table-hover table-bordered"
                    >
                        <thead className="table-dark text-center">
                            <tr>
                                <th scope="col">
                                    <span class="badge text-bg-dark"><h6><b>Trigger Price</b></h6></span><br />
                                    <span className="bi bi-currency-rupee"></span>
                                </th>
                                <th scope="col">
                                    <span class="badge text-bg-dark"><h6><b>Trigger Date</b></h6></span><br />
                                    <span className="bi bi-calendar"></span>
                                </th>
                                <th scope="col">
                                    <span class="badge text-bg-dark"><h6><b>Trigger Time</b></h6></span><br />
                                    <span className="bi bi-clock"></span>
                                </th>
                                <th scope="col">
                                    <span class="badge text-bg-dark"><h6><b>Scans</b></h6></span><br />
                                    <span className="bi bi-upc-scan"></span>
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            {archives.length ? (
                                archives.map((archive, index) => (
                                    <tr key={index} className="text-left">
                                        <td align="center" class="align-middle"><span class="badge text-bg-secondary">{archive.trigger_price}</span></td>
                                        <td align="center" class="align-middle"><span class="badge text-bg-secondary">{archive.triggered_date}</span></td>
                                        <td align="center" class="align-middle"><span class="badge text-bg-secondary">{archive.triggered_at}</span></td>
                                        <td align="center" class="align-middle">
                                            <span class={archive.scan_name.includes('long') ? 'badge text-bg-success' : 'badge text-bg-danger'}>{archive.scan_name.trim()}</span>
                                        </td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan="5" className="text-center">
                                        <div class="d-flex align-items-center">
                                            <strong role="status">No history date found.</strong>
                                        </div>
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            </div>
        </>
    )
};

export default ArchiveComponent;
