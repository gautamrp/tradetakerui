import React, { useState } from "react";
import { read, utils, writeFile } from "xlsx";
import axios from "axios";
import constants from "../config/constants";

const TradeComponent = () => {
  const [trades, setTrades] = useState([]);
  const [response, setResponse] = React.useState(false);

  // Import the trades from Excel
  const handleImport = ($event) => {
    console.log("import");
    const files = $event.target.files;
    if (files.length) {
      const file = files[0];
      const reader = new FileReader();
      reader.onload = (event) => {
        const wb = read(event.target.result);
        const sheets = wb.SheetNames;

        if (sheets.length) {
          let rows = utils.sheet_to_json(wb.Sheets[sheets[0]], {
            raw: false,
          });
          rows = rows.map((trade, index) => ({
            ...trade,
            sl: "Default",
            trigger: "Default",
            quantity: "Default",
            gtt: "Default",
            tradeType: "MIS",
            status: "",
            selected: false,
          }));
          console.log(rows);
          setTrades(rows);
        }
      };
      reader.readAsArrayBuffer(file);
    }
  };

  // Export the trandes to excel
  const handleExport = () => {
    const headings = [
      ["Symbol", "LTP", "SL", "Trigger", "Quantity", "GTT", "Trade Type"],
    ];
    const wb = utils.book_new();
    const ws = utils.json_to_sheet([]);
    utils.sheet_add_aoa(ws, headings);
    utils.sheet_add_json(ws, trades, { origin: "A2", skipHeader: true });
    utils.book_append_sheet(wb, ws, "Report");
    writeFile(wb, "Trade Report.xlsx");
  };

  const executeSelected = (site) => {
    const selectedTrades = [];
    trades.forEach((trade) => {
      if (trade.selected) selectedTrades.push(trade);
    });

    const options = {
      mode: "no-cors",
      headers: {
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Headers": "*",
        "Access-Control-Allow-Credentials": "true",
      },
    };

    axios.post(constants.baseURL + site, selectedTrades, options).then(
      (response) => {
        setTrades(response.data);
        setResponse(true);
      },
      (error) => {
        console.log(error);
      }
    );
  };

  // Select the trades
  const selectTrades = (position) => {
    setTrades(
      trades.map((trade, index) => ({
        ...trade,
        selected:
          index === position && trade.selected
            ? false
            : index === position && !trade.selected
            ? true
            : trade.selected,
      }))
    );
  };

  // Select all trades
  const selectAllTrades = (e) => {
    console.log(e.target.checked);
    setTrades(
      trades.map((trade, index) => ({
        ...trade,
        selected: e.target.checked,
      }))
    );
  };

  const searchSymbol = () => {
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("mytrades");
    filter = input.value.toUpperCase();
    table = document.getElementById("trades");
    tr = table.getElementsByTagName("tr");

    // Loop through all table rows, and hide those who don't match the search query
    for (i = 0; i < tr.length; i++) {
      td = tr[i].getElementsByTagName("td")[0];
      if (td) {
        txtValue = td.textContent || td.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
          tr[i].style.display = "";
        } else {
          tr[i].style.display = "none";
        }
      }
    }
  };

  return (
    <>
      <div className="row mb-2 mt-5">
        <div className="col-sm-6 offset-2">
          <div className="row">
            <div className="col-md-6">
              <div className="input-group">
                <div className="custom-file">
                  <input
                    type="file"
                    name="file"
                    className="custom-file-input"
                    id="inputGroupFile"
                    required
                    disabled={response}
                    onChange={handleImport}
                    accept=".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"
                  />
                  <label className="custom-file-label" htmlFor="inputGroupFile">
                    Choose file
                  </label>
                </div>
              </div>
            </div>
            <div className="col-md-6">
              <button
                onClick={handleExport}
                className="btn btn btn-primary btn-sm mr-3"
              >
                Export <i className="fa fa-download"></i>
              </button>
              <button
                disabled={response}
                onClick={() => executeSelected("placeorder")}
                className="btn btn btn-primary btn-sm mr-3"
              >
                Execute on TT
              </button>
              <button
                disabled={response}
                onClick={() => executeSelected("placezorder")}
                className="btn btn btn-primary btn-sm mr-3"
              >
                Execute on Kite
              </button>
              <br />
              <br />
            </div>
          </div>
        </div>
      </div>
      <div className="row">
        <div className="col-sm-8 offset-2">
          <input
            type="text"
            id="mytrades"
            onKeyUp={() => searchSymbol()}
            placeholder="Search for Symbol.."
          />
          <br />
          <br />
          <table
            id="trades"
            className="table table-striped table-hover table-bordered"
          >
            <thead className="thead-dark text-center">
              <tr>
                <th scope="col">ID</th>
                <th scope="col">Symbol</th>
                <th scope="col">LTP</th>
                <th scope="col">SL</th>
                <th scope="col">Trigger</th>
                <th scope="col">Quantity</th>
                <th scope="col">GTT</th>
                <th scope="col">Trade Type</th>
                <th scope="col">
                  Selection
                  <div className="form-check form-switch">
                    <input
                      className="form-check-input"
                      type="checkbox"
                      id="flexSwitchCheckChecked"
                      //checked={trades.length === 0 || response ? true : false}
                      disabled={trades.length === 0 || response ? true : false}
                      onChange={(e) => selectAllTrades(e)}
                    />
                  </div>
                </th>
                <th scope="col">Status</th>
              </tr>
            </thead>
            <tbody>
              {trades.length ? (
                trades.map((trade, index) => (
                  <tr key={index} className="text-center">
                    <th scope="row">{index + 1}</th>
                    <td>
                      <a
                        target="_blank"
                        rel="noopener noreferrer"
                        href={
                          "https://www.nseindia.com/get-quotes/equity?symbol=" +
                          trade.symbol
                        }
                        className="link-primary"
                      >
                        <h4>
                          <span className="badge badge-secondary">
                            {trade.symbol}
                          </span>
                        </h4>
                      </a>
                    </td>{" "}
                    <td>{trade.ltp}</td>
                    <td>Default</td>
                    <td>Default</td>
                    <td>Default</td>
                    <td>Default</td>
                    <td>Default</td>
                    <td>
                      <div className="form-check form-switch">
                        <input
                          className="form-check-input"
                          type="checkbox"
                          value="{trade.Symbol}"
                          id={`custom-checkbox-${index}`}
                          aria-label="..."
                          disabled={response}
                          checked={trade.selected}
                          onChange={() => selectTrades(index)}
                        />
                      </div>
                    </td>
                    <td>
                      <button
                        type="button"
                        className={`btn ${
                          trade.status === "Order Placed"
                            ? "btn-success"
                            : trade.status === "Order Failed"
                            ? "btn-danger"
                            : "btn-secondary"
                        }`}
                        disabled
                      >
                        {trade.status === "Order Placed"
                          ? "Success"
                          : trade.status === "Order Failed"
                          ? "Failed"
                          : "Waiting"}
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="5" className="text-center">
                    No Trades Found.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </>
  );
};

export default TradeComponent;
