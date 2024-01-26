import React, { useEffect } from "react";
import axios from "axios";
import constants from "../config/constants";
import ArchiveComponent from "./archive.compoent";

const LevelsComponent = () => {
  const [levels, setLevels] = React.useState(false);
  const [dates, setDates] = React.useState(false);
  const [date, setDate] = React.useState(" Not selected");
  const [extractdate, setExtractDate] = React.useState(false);
  const [archives, setArchives] = React.useState('');

  useEffect(() => {
    axios.get(constants.baseURL + "expirydates").then((response) => {
      setDates(response.data);
    });

    axios.get(constants.baseURL + "extractdate").then((response) => {
      setExtractDate(response.data);
    });
  }, []);

  // Select the trades
  const reloadLevels = (position) => {
    setLevels(null);
    axios
      .get(constants.baseURL + "levels/" + dates[position])
      .then((response) => {
        setLevels(response.data);
      });
    setDate(dates[position]);
  };

  const searchInTable = (element, id) => {
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById(element);
    filter = input.value.toUpperCase();
    table = document.getElementById("levels");
    tr = table.getElementsByTagName("tr");

    // Loop through all table rows, and hide those who don't match the search query
    for (i = 0; i < tr.length; i++) {
      td = tr[i].getElementsByTagName("td")[id];
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

  const getArchivesData = (index) => {
    console.log(index)
    console.log(levels[index].symbol.trim())
    axios.get(constants.baseURL + "archives/" + levels[index].symbol.trim()).then((response) => {
      setArchives(response.data);
    });
  };

  return (
    <>
      <div className="row mb-2 mt-5">
        <div className="col-sm-6 offset-2">
          <div className="row">
            <div className="col-md-6">
              <div class="btn-group">
                <button type="button" class="btn btn-primary dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                  Expiry Date
                </button>
                <ul class="dropdown-menu">
                  {dates.length
                    ? dates.map((date, index) => (
                      <li>
                        <button
                          key={`custom-dates-${index}`}
                          href="/#"
                          className="dropdown-item"
                          onClick={() => reloadLevels(index)}
                        >
                          {date}
                        </button>
                      </li>
                    ))
                    : null}
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className="row">
        <div className="col-sm-8 offset-2 table-responsive">
          <input
            type="text"
            id="mysymbol"
            onKeyUp={() => searchInTable("mysymbol", 1)}
            placeholder="Search for Symbol.."
          />
          <br />
          <br />
          <div class="accordion" id="accordionExample">
            <div class="accordion-item">
              <h2 class="accordion-header" id="headingOne">
                <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                  <span><h6><b>Expiry date : {date} &nbsp;&nbsp;&nbsp;&nbsp; Last Extract Date: {extractdate}&nbsp;(Daily 9PM IST)</b></h6></span><br />
                </button>
              </h2>
              <div id="collapseOne" class="accordion-collapse collapse show" aria-labelledby="headingOne" data-bs-parent="#accordionExample">
                <div class="accordion-body">
                  <div class="table-responsive">
                    <table
                      id="levels"
                      className="table table-striped table-hover table-bordered"
                    >
                      <thead className="table-dark text-center">
                        <tr>
                          <th scope="col">
                            <span class="badge text-bg-dark"><h6><b>ID</b></h6></span><br />
                            <span className="bi bi-hash"></span>
                          </th>
                          <th scope="col">
                            <span class="badge text-bg-dark"><h6><b>Symbol</b></h6></span><br />
                            <span className="bi bi-bank"></span>
                          </th>
                          <th scope="col">
                            <span class="badge text-bg-dark"><h6><b>Support[PE]</b></h6></span><br />
                            <span className="bi bi-reception-2"></span>
                          </th>
                          <th scope="col">
                            <span class="badge text-bg-dark"><h6><b>Support[PE]</b></h6></span><br />
                            <span className="bi bi-reception-3"></span>
                          </th>
                          <th scope="col">
                            <span class="badge text-bg-dark"><h6><b>Support[PE]</b></h6></span><br />
                            <span className="bi bi-reception-4"></span>
                          </th>
                          <th scope="col">
                            <span class="badge text-bg-dark"><h6><b>Resistance[CE]</b></h6></span><br />
                            <span className="bi bi-reception-4"></span>
                          </th>
                          <th scope="col">
                            <span class="badge text-bg-dark"><h6><b>Resistance[CE]</b></h6></span><br />
                            <span className="bi bi-reception-3"></span>
                          </th>
                          <th scope="col">
                            <span class="badge text-bg-dark"><h6><b>Resistance[CE]</b></h6></span><br />
                            <span className="bi bi-reception-2"></span>
                          </th>
                        </tr>
                      </thead>
                      <tbody>
                        {levels !== null && levels.length ? (
                          levels.map((level, index) => (
                            <tr key={index} className="text-center">
                              <td><span class="badge text-bg-dark">{index + 1}</span></td>
                              <td align="left">
                                <picture><img class="img-thumbnail" src={'assets/img/svg/' + level.symbol.trim() + '.svg'} alt={level.symbol.trim()} /></picture>
                                &nbsp;&nbsp;
                                <a
                                  target="_blank"
                                  rel="noopener noreferrer"
                                  href={
                                    "https://www.nseindia.com/get-quotes/equity?symbol=" +
                                    level.symbol.trim()
                                  }
                                  className="link-primary"
                                >
                                  <span class="badge text-bg-warning">
                                    <b>{level.symbol.trim()}</b>
                                  </span>
                                </a>
                                <hr></hr>
                                <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                  <div class="modal-dialog">
                                    <div class="modal-content">
                                      <div class="modal-header">
                                        <b>History Triggers for&nbsp;</b> <span class="badge text-bg-warning">
                                          <b>{level.symbol}</b>
                                        </span>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                      </div>
                                      <div class="modal-body">
                                        <ArchiveComponent archives={archives} />
                                      </div>
                                    </div>
                                  </div>
                                </div>
                                <a
                                  type="button"
                                  href="/#"
                                  title="History"
                                  className="bi bi-archive-fill"
                                  onClick={() => getArchivesData(index)}
                                  data-bs-toggle="modal" data-bs-target="#exampleModal"
                                ></a>
                                &nbsp;&nbsp;
                                <a
                                  target="_blank"
                                  rel="noopener noreferrer"
                                  type="button"
                                  href={constants.googleFinanceURL + level.symbol.trim() + ':NSE'}
                                  title="Check on Google Finance"
                                  className="bi bi-google"
                                >
                                </a>
                                &nbsp;&nbsp;
                                <a
                                  target="_blank"
                                  rel="noopener noreferrer"
                                  type="button"
                                  title="Check on Trading View"
                                  href={constants.tradingViewURL + level.symbol.trim()}
                                >
                                  <img Style="max-width:9%;" src="https://static.tradingview.com/static/images/favicon.ico" alt="Trading View" />
                                </a>
                              </td>
                              <td> <span class="badge text-bg-success">{level.support3}</span></td>
                              <td> <span class="badge text-bg-success">{level.support2}</span></td>
                              <td> <span class="badge text-bg-success">{level.support1}</span></td>
                              <td> <span class="badge text-bg-danger">{level.resistance1}</span></td>
                              <td> <span class="badge text-bg-danger">{level.resistance2}</span></td>
                              <td> <span class="badge text-bg-danger">{level.resistance3}</span></td>
                            </tr>
                          ))
                        ) : (
                          <tr>
                            <td colSpan="5" className="text-center">
                              {date.trim() !== "Not selected" ? (
                                <div class="d-flex align-items-center">
                                  <strong role="status">Loading the data for {date}...</strong>
                                  <div class="spinner-border ms-auto" aria-hidden="true"></div>
                                </div>
                              ) : (
                                <div class="d-flex align-items-center">
                                  <strong role="status">Please elect expiry date from dropdown...</strong>
                                </div>
                              )}
                            </td>
                          </tr>
                        )}
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default LevelsComponent;
