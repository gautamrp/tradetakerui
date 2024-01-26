import React, { useEffect } from "react";
import axios from "axios";
import constants from "../config/constants";
import ArchiveComponent from "./archive.compoent";

const TriggersComponent = () => {
  const [triggers, setTriggers] = React.useState(false);
  const [post, setPost] = React.useState(" Click to verify");
  const [dates, setDates] = React.useState(false);
  const [date, setDate] = React.useState(" Not selected");
  //const [time, setTime] = React.useState(Date.now());
  const [archives, setArchives] = React.useState('');
  const filters = ([
    "Show All",
    "Between Support(S1) and Resistance(R1)",
    "Between Support(S2) and Resistance(R2)",
    "Above Resistance(R1)",
    "Above Resistance(R2)",
    "Below Support(S1)",
    "Below Support(S2)",
    "Between Supports(S1,S2)",
    "Between Resistances(R1,R2)"]
  );
  const [filter, setFilter] = React.useState(0);

  const healthCheck = () => {
    axios.get(constants.baseURL + "health").then((response) => {
      setPost(response.data);
    });
  };

  useEffect(() => {
    axios.get(constants.baseURL + "triggerdates").then((response) => {
      setDates(response.data);
    });

    // const interval = setInterval(() => {
    //   setTime(Date.now());
    // }, 1000);
    // return () => {
    //   clearInterval(interval);
    // };
  }, []);

  // Select the trades
  const reloadTrigger = (position) => {
    setTriggers(null);
    axios
      .get(constants.baseURL + "trigger/" + dates[position])
      .then((response) => {
        setTriggers(response.data);
      });
    setDate(dates[position]);
  };

  // const postToTelegram = (trigger) => {
  //   axios.post(constants.baseURL + "telegram", trigger).then(
  //     (response) => { },
  //     (error) => {
  //       console.log(error);
  //     }
  //   );
  // };

  const getArchivesData = (symbol) => {
    axios.get(constants.baseURL + "archives/" + symbol).then((response) => {
      setArchives(response.data);
    });
  };

  const searchInTable = (element, id) => {
    var input, filter, table, tr, td, i, txtValue;
    setFilter(0)

    input = document.getElementById(element);
    filter = input.value.toUpperCase();
    table = document.getElementById("triggers");
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

  const filterTrigger = (option) => {
    var supportLevel, restistanceLevel;

    triggers.forEach((trigger, index) => {
      supportLevel = trigger.support.split(' ~ ')
      restistanceLevel = trigger.resistance.split(' ~ ')
      setFilter(Number(option.index));

      switch (Number(option.index)) {
        case 0: // Show All
          document.getElementById('trigger' + index).style.display = "";
          break;
        case 1: // Between Support(S1) and Resistance(R1)
          if (trigger.trigger_price >= supportLevel[1] && trigger.trigger_price <= restistanceLevel[1]) {
            document.getElementById('trigger' + index).style.display = "";
          } else {
            document.getElementById('trigger' + index).style.display = "none";
          }
          break;
        case 2: // Between Support(S2) and Resistance(R2)
          if (trigger.trigger_price >= supportLevel[2] && trigger.trigger_price <= restistanceLevel[2]) {
            document.getElementById('trigger' + index).style.display = "";
          } else {
            document.getElementById('trigger' + index).style.display = "none";
          }
          break;
        case 3:  // Above Resistance(R1)
          if (trigger.trigger_price >= restistanceLevel[1]) {
            document.getElementById('trigger' + index).style.display = "";
          } else {
            document.getElementById('trigger' + index).style.display = "none";
          }
          break;
        case 4:  // Above Resistance(R2)
          if (trigger.trigger_price >= restistanceLevel[2]) {
            document.getElementById('trigger' + index).style.display = "";
          } else {
            document.getElementById('trigger' + index).style.display = "none";
          }
          break;
        case 5:  // Below Support(S1)
          if (trigger.trigger_price >= supportLevel[1]) {
            document.getElementById('trigger' + index).style.display = "";
          } else {
            document.getElementById('trigger' + index).style.display = "none";
          }
          break;
        case 6:  // Below Support(S2)
          if (trigger.trigger_price >= supportLevel[2]) {
            document.getElementById('trigger' + index).style.display = "";
          } else {
            document.getElementById('trigger' + index).style.display = "none";
          }
          break;
        case 7:  // Between Supports(S1,S2)
          if (trigger.trigger_price >= supportLevel[1] && trigger.trigger_price <= supportLevel[2]) {
            document.getElementById('trigger' + index).style.display = "";
          } else {
            document.getElementById('trigger' + index).style.display = "none";
          }
          break;
        case 8:  // Between Resistances(R1,R2)
          if (trigger.trigger_price >= restistanceLevel[1] && trigger.trigger_price <= restistanceLevel[2]) {
            document.getElementById('trigger' + index).style.display = "";
          } else {
            document.getElementById('trigger' + index).style.display = "none";
          }
          break;
        default:
          break;
      }

    })
  };

  return (
    <>
      <div className="row mb-2 mt-5">
        <div className="col-sm-6 offset-2">
          <button
            onClick={healthCheck}
            type="button"
            className="btn btn-primary btn-sm mr-3"
          >
            API Health - {post === ' Click to verify' ? ' Click to verify' : (post === 'API is up and running.' ? <span className="bi bi-check2-circle"></span> : <span className="bi bi-x-circle"></span>)}
          </button>
          <br />
          <br />
          <div className="row">
            <div className="col-md-6">
              <div class="btn-group">
                <button type="button" class="btn btn-primary dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                  Triggers Dates
                </button>
                <ul class="dropdown-menu">
                  {dates.length
                    ? dates.map((date, index) => (
                      <li>
                        <button
                          key={`custom-dates-${index}`}
                          href="/#"
                          className="dropdown-item"
                          onClick={() => reloadTrigger(index)}
                        >
                          {date}
                        </button>
                      </li>
                    ))
                    : null}
                </ul>
                &nbsp;&nbsp;&nbsp;
                <button type="button" 
                className="btn btn-primary bi-arrow-clockwise"
                 aria-expanded="false" 
                 onClick={() => reloadTrigger(0)} 
                 title="Refresh data for latest date"></button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className="col-sm-8 offset-2">
        <input
          type="text"
          id="mytrigger"
          onKeyUp={() => searchInTable("mytrigger", 1)}
          disabled={triggers !== null && triggers.length ? false : true}
          placeholder="Search for Symbol.."
        />
        &nbsp;&nbsp;
        <input
          type="text"
          id="myscan"
          onKeyUp={() => searchInTable("myscan", 2)}
          disabled={triggers !== null && triggers.length ? false : true}
          placeholder="Search for Scan.."
        />
        &nbsp;&nbsp;
        <div class="btn-group">
          <button type="dropdown" class="btn btn-primary dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false" disabled={triggers !== null && triggers.length ? false : true}>
            Select Filter
          </button>
          <ul id="filter" class="dropdown-menu">
            {filters.map((filter, index) => (
              <li><button onClick={() => filterTrigger({ index })} class="dropdown-item" href="/#">{filter}</button></li>
            ))}
          </ul>
        </div>
        <br /><br />
        <div class="accordion" id="accordionExample">
          <div class="accordion-item">
            <h2 class="accordion-header" id="headingOne">
              <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                <span><h6><b>Trigger date : {date} &nbsp; Filter: {filters[filter]}</b></h6></span>
              </button>
            </h2>
            <div id="collapseOne" class="accordion-collapse collapse show" aria-labelledby="headingOne" data-bs-parent="#accordionExample">
              <div class="accordion-body">
                <div class="table-responsive">
                  <div class="modal fade" id="exampleModal" tabIndex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                      <div class="modal-content">
                        <div class="modal-header">
                          <b>History Triggers for&nbsp;</b> <span class="badge text-bg-warning">
                          </span>
                          <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                          <ArchiveComponent archives={archives} />
                        </div>
                      </div>
                    </div>
                  </div>
                  <table
                    id="triggers"
                    className="table table-striped table-hover table-bordered table-responsive table-fit"
                  >
                    <thead className="table-dark text-center">
                      <tr>
                        <th scope="col">
                          <span class="badge text-bg-dark"><h6><b>ID</b></h6></span><br />
                          <span className="bi bi-hash"></span>
                        </th>
                        <th scope="col">
                          <span class="badge text-bg-dark"><h6><b>Stock</b></h6></span><br />
                          <span className="bi bi-bank"></span>
                        </th>
                        <th scope="col">
                          <span class="badge text-bg-dark"><h6><b>Trigger</b></h6></span><br />
                          <span className="bi bi-currency-rupee"></span>
                          <span className="bi bi-clock"></span>
                        </th>
                        <th scope="col">
                          <span class="badge text-bg-dark"><h6><b>Scans</b></h6></span><br />
                          <span className="bi bi-upc-scan"></span>
                        </th>
                        <th scope="col">
                          <span class="badge text-bg-dark"><h6><b>Support/Resistance</b></h6></span><br />
                          <span className="bi bi-align-bottom"></span>
                          <span className="bi bi-align-top"></span>
                        </th>
                        <th scope="col">
                          <span class="badge text-bg-dark"><h6><b>Calls</b></h6></span><br />
                          <span className="bi bi-megaphone"></span>
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      {triggers !== null && triggers.length ? (
                        triggers.map((trigger, index) => (
                          <tr id={'trigger' + index} key={index} className="text-left">
                            <td><span class="badge text-bg-dark">{index + 1}</span></td>
                            <td align="left" class="align-middle">
                              <picture><img class="img-thumbnail" src={'assets/img/svg/' + trigger.stock.trim() + '.svg'} alt={trigger.stock.trim()} /></picture>
                              &nbsp;&nbsp;
                              <a
                                target="_blank"
                                rel="noopener noreferrer"
                                href={
                                  "https://www.nseindia.com/get-quotes/derivatives?symbol=" +
                                  trigger.stock.trim()
                                }
                                className="link-primary"
                              >
                                <span class="badge text-bg-warning">
                                  <b>{trigger.stock}</b>
                                </span>
                              </a>
                              &nbsp;&nbsp;
                              <hr></hr>
                              <a
                                type="button"
                                href="/#"
                                title="History"
                                className="bi bi-archive-fill bi-primary"
                                onClick={() => getArchivesData(trigger.stock.trim())}
                                data-bs-toggle="modal" data-bs-target="#exampleModal"
                              ></a>
                              {/* <a
                                type="button"
                                href="/#"
                                title="Post to Telegram"
                                className="bi bi-telegram bi-primary"
                                onClick={() => postToTelegram(trigger)}
                              ></a> */}
                              &nbsp;&nbsp;
                              <a
                                target="_blank"
                                rel="noopener noreferrer"
                                type="button"
                                href={constants.googleFinanceURL + trigger.stock.trim() + ':NSE'}
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
                                href={constants.tradingViewURL + trigger.stock.trim()}
                              >
                                <img Style="max-width:9%;" src="https://static.tradingview.com/static/images/favicon.ico" alt="Trading View" />
                              </a>

                            </td>{" "}
                            <td align="center" class="align-middle">
                              <span id={'price' + index} class="badge text-bg-secondary">{trigger.trigger_price}</span>
                              <hr></hr>
                              <span id={'at' + index} class="badge text-bg-secondary">{trigger.triggered_at}</span>
                            </td>
                            <td align="center" class="align-middle">
                              <span class={trigger.scan_name.includes('long') ? 'badge text-bg-success' : 'badge text-bg-danger'}>{trigger.scan_name.trim()}</span>
                            </td>
                            <td align="center" class="align-middle">
                              <span id={'support' + index} class="badge text-bg-success">{trigger.support}</span>
                              <hr ></hr>
                              <span id={'resistance' + index} class="badge text-bg-danger">{trigger.resistance}</span>
                            </td>
                            <td align="left" >
                              <b>
                                <span class="text-primary">{trigger.calls[2]}-</span><span class="text-primary">{trigger.settle_pr[2]}-</span>{trigger.scan_name.trim() === "longFUT" ? <span class="text-primary">BUY</span> : <span class="text-primary">SELL</span>}<br />
                                <span class="text-danger">{trigger.calls[3]}-</span><span class="text-danger">{trigger.settle_pr[3]}-</span>{trigger.scan_name.trim() !== "longFUT" ? <span class="text-danger">BUY</span> : <span class="text-danger">SELL </span>}<br />
                                <hr ></hr>
                                <span class="text-primary">{trigger.calls[0]}-</span><span class="text-primary">{trigger.settle_pr[0]}-</span>{trigger.scan_name.trim() === "longFUT" ? <span class="text-primary">BUY</span> : <span class="text-primary">SELL</span>} <br />
                                <span class="text-danger">{trigger.calls[1]}-</span><span class="text-danger">{trigger.settle_pr[1]}-</span>{trigger.scan_name.trim() !== "longFUT" ? <span class="text-danger">BUY</span> : <span class="text-danger">SELL</span>}<br />
                              </b>
                            </td>
                          </tr>
                        ))
                      ) : (
                        <tr>
                          <td colSpan="5" className="text-center">
                            {date.trim() !== "Not selected" ? (
                              <div class="d-flex align-items-center">
                                <strong role="status">Loading the triggers for {date}...</strong>
                                <div class="spinner-border ms-auto" aria-hidden="true"></div>
                              </div>
                            ) : (
                              <div class="d-flex align-items-center">
                                <strong role="status">Please select the trigger date...</strong>
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
    </>
  );
};

export default TriggersComponent;
