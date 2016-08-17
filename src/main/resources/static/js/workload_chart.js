/**
 * Created by George on 13.08.2016.
 */
AmCharts.useUTC = true;
/*  go chart */
var chart = AmCharts.makeChart( "chartdiv", {
    "type": "gantt",
    "theme": "dark",
    "marginRight": 70,
    "period": "DD",
    "dataDateFormat": "YYYY-MM-DD",
    "balloonDateFormat": "JJ:NN",
    "columnWidth": 0.5,
    "valueAxis": {
        "type": "date",
        "minimum": 0,
        "maximum": 31
    },
    "brightnessStep": 10,
    "graph": {
        "fillAlphas": 1,
        "balloonText": "<b>[[employee_name]]</b>:Start: [[open]] End: [[value]] <p>Загруженность: [[employee_workload]]%</p>"
    },
    "rotate": true,
    "categoryField": "employee_name",
    "segmentsField": "project",
    "colorField": "color",
    "startDate": "2016-01-01",
    "startField": "start",
    "endField": "end",
    "durationField": "duration",
    "dataProvider": [ {
        "employer_id": "--",
        "employee_name": "John",
        "project": [ {
            "project_id": "---",
            "start": 0,
            "duration": 12,
            "color": "#0B2161",
            "project_name": "",
            "employee_workload": "100"
        },
            {
                "start": 13,
                "duration": 7,
                "color": "#0B2161",
                "employee_workload": "95"

            }]
    }, {
        "employer_id": "--",
        "employee_name": "Smith",
        "project": [ {
            "project_id": "---",
            "start": 0,
            "duration": 15,
            "color": "#045FB4",
            "project_name": "",
            "employee_workload": "85"
        }]
    }, {
        "employer_id": "--",
        "employee_name": "Ben",
        "project": [ {
            "project_id": "---",
            "start": 0,
            "duration": 10,
            "color": "#045FB4",
            "project_name": "",
            "employee_workload": "70"
        }]
    }, {
        "employer_id": "--",
        "employee_name": "Mike",
        "project": [ {
            "project_id": "---",
            "start": 0,
            "duration": 6,
            "color": "#2E9AFE",
            "project_name": "",
            "employee_workload": "55"
        }]
    }, {
        "employer_id": "--",
        "employee_name": "Lenny",
        "project": [ {
            "project_id": "---",
            "start": 0,
            "duration": 4,
            "color": "#2E9AFE",
            "project_name": "",
            "employee_workload": "40"
        }]
    }, {
        "employer_id": "--",
        "employee_name": "Scott",
        "project": [ {
            "project_id": "---",
            "start": 0,
            "duration": 2,
            "color": "#A9D0F5",
            "project_name": "",
            "employee_workload": "25"
        } ]
    }, {
        "employer_id": "--",
        "employee_name": "Julia",
        "project": [ {
            "project_id": "---",
            "start": 0,
            "duration": 1,
            "color": "#A9D0F5",
            "project_name": "",
            "employee_workload": "10"
        }]
    }, {
        "employer_id": "--",
        "employee_name": "Bob",
        "project": [ {
            "project_id": "---",
            "start": 0,
            "duration": 9,
            "color": "#A9D0F5",
            "project_name": "",
            "employee_workload": "15"
        }]
    }, {
        "employer_id": "--",
        "employee_name": "Kendra",
        "project": [ {
            "project_id": "---",
            "start": 0,
            "duration": 8,
            "color": "#A9D0F5",
            "project_name": "",
            "employee_workload": "25"
        }]
    }, {
        "employer_id": "--",
        "employee_name": "Tom",
        "project": [ {
            "project_id": "---",
            "start": 0,
            "duration": 5,
            "color": "#2E9AFE",
            "project_name": "",
            "employee_workload": "45"
        }]
    }, {
        "employer_id": "--",
        "employee_name": "Kyle",
        "project": [ {
            "project_id": "---",
            "start": 0,
            "duration": 8,
            "color": "#045FB4",
            "project_name": "",
            "employee_workload": "75"
        } ]
    }, {
        "employer_id": "--",
        "employee_name": "Anita",
        "project": [ {
            "project_id": "---",
            "start": 0,
            "duration": 6,
            "color": "#2E9AFE",
            "project_name": "",
            "employee_workload": "50"
        }]
    }, {
        "employer_id": "--",
        "employee_name": "Jack",
        "project": [ {
            "project_id": "---",
            "start": 0,
            "duration": 15,
            "color": "#0B2161",
            "project_name": "",
            "employee_workload": "90"
        }]
    }, {
        "employer_id": "--",
        "employee_name": "Kim",
        "project": [ {
            "project_id": "---",
            "start": 0,
            "duration": 10,
            "color": "#045FB4",
            "project_name": "",
            "employee_workload": "85"
        }]
    }, {
        "employer_id": "--",
        "employee_name": "Aaron",
        "project": [ {
            "project_id": "---",
            "start": 0,
            "duration": 12,
            "color": "#0B2161",
            "project_name": "",
            "employee_workload": "100"
        }]
    }, {
        "employer_id": "--",
        "employee_name": "Alan",
        "project": [ {
            "project_id": "---",
            "start": 0,
            "duration": 7,
            "color": "#045FB4",
            "project_name": "",
            "employee_workload": "70"
        }]
    }, {
        "employer_id": "--",
        "employee_name": "Ruth",
        "project": [ {
            "project_id": "---",
            "start": 0,
            "duration": 9,
            "color": "#2E9AFE",
            "project_name": "",
            "employee_workload": "40"
        }]
    }, {
        "employer_id": "--",
        "employee_name": "Simon",
        "project": [ {
            "project_id": "---",
            "start": 0,
            "duration": 13,
            "color": "#A9D0F5",
            "project_name": "",
            "employee_workload": "10"
        }]
    } ],
    "valueScrollbar": {
        "autoGridCount": true
    },
    "chartCursor": {
        "cursorColor":"#55bb76",
        "valueBalloonsEnabled": false,
        "cursorAlpha": 0,
        "valueLineAlpha":0.5,
        "valueLineBalloonEnabled": true,
        "valueLineEnabled": true,
        "zoomable":false,
        "valueZoomable": true
    },
    "export": {
        "enabled": true
    }
} );