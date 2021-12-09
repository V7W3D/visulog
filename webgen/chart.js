const myData = [{
        type: "line",
        toolTipContent: "<b>{label}</b>: {y} commits",
        showInLegend: "true",
        indexLabelFontSize: 16,
        dataPoints: [/*data*/]
}];

const myConfig = {
        theme: "light2",        exportEnabled: true,
        animationEnabled: true,
        title: {
                text: "/*title*/"
        },
        data : myData
};

var myChart = new CanvasJS.Chart("chartContainer", myConfig);
