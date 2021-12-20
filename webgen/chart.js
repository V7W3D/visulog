CanvasJS.addColorSet("blueShades",
        [
        "#87CEEB",
        "#33A1C9",
        "#00BFFF",
        "#00B2EE",
        "#0099CC",
        "#009ACD",
        "#38B0DE",       
        "#33A1DE",
        "#0EBFE9"     
        ]);   
const myData = [{
        type: "column",
        toolTipContent: "<b>{label}</b>: {y} commits",
        showInLegend: false,
        dataPoints: [/*data*/]
}];

const myConfig = {
        colorSet:"blueShades",
        theme: "dark1",        exportEnabled: true,
        zoomEnabled:true,
        animationEnabled: true,
        title: {
                text: "/*title*/"
        },
        axisY:{
                labelFontSize: 20,
        },
        axisX:{
                interval : 1,
                labelFontSize: 15,
                labelAngle: -60,
        },
        data : myData
};

var myChart = new CanvasJS.Chart("chartContainer", myConfig);
