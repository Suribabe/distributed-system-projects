<%-- 
    Document   : result
    Created on : Nov 7, 2016, 8:59:09 PM
    Author     : Jingshiqing
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="task2.ConnectMongoDB"%>

<!DOCTYPE html>

<html>
    <head>
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
        <link rel="stylesheet" type="text/css" href="bootstrap.css">
        <title>Interesting Picture</title>
    </head>
    <body>
        <div class="container">
            <div class="row">
                <div class="col-xs-5"></div>
                <div class="col-xs-2"><h1>Dashboard</h1></div>
                <div class="col-xs-5"></div>     
            </div>
            <% ConnectMongoDB connectMongoDB = new ConnectMongoDB();
                connectMongoDB.dashborad(); %>
            <br/>
            <div class="row">
                <div class="col-xs-6"> 
                    <h4>Top Five Course Search Terms</h4>
                    <span class="label label-default" height="30px"><% out.print(connectMongoDB.getMax_client_request()[0]);%></span>
                    <span class="label label-success" height="30px"><% out.print(connectMongoDB.getMax_client_request()[1]);%></span>
                    <span class="label label-danger" height="30px"><% out.print(connectMongoDB.getMax_client_request()[2]);%></span>
                    <span class="label label-warning" height="30px"><% out.print(connectMongoDB.getMax_client_request()[3]);%></span>
                    <span class="label label-primary" height="30px"><% out.print(connectMongoDB.getMax_client_request()[4]);%></span>
                </div>
                <div class="col-xs-6">
                    <h4>Top One Client Model</h4>
                    <p><% out.print(connectMongoDB.getTop_user_agent());%></p>
                </div>
                <hr><hr><hr><br/>
            </div>
            <div class="row">
                <div class="col-xs-6">
                    <h4>Average Time Latency</h4>
                    <p><% out.print(connectMongoDB.getLatency() + "ms");%></p>
                </div>
                <div class="col-xs-6">
                    <h4>Top Ten Popular Course</h4>
                    <span class="label label-default" height="30px"><% out.print(connectMongoDB.getPopular_course()[0]);%></span>
                    <span class="label label-success" height="30px"><% out.print(connectMongoDB.getPopular_course()[1]);%></span>
                    <span class="label label-danger" height="30px"><% out.print(connectMongoDB.getPopular_course()[2]);%></span>
                    <span class="label label-warning" height="30px"><% out.print(connectMongoDB.getPopular_course()[3]);%></span>
                    <span class="label label-primary" height="30px"><% out.print(connectMongoDB.getPopular_course()[4]);%></span>
                    <span class="label label-default" height="30px"><% out.print(connectMongoDB.getPopular_course()[5]);%></span>
                    <span class="label label-info" height="30px"><% out.print(connectMongoDB.getPopular_course()[6]);%></span>
                    <span class="label label-danger" height="30px"><% out.print(connectMongoDB.getPopular_course()[7]);%></span>
                    <span class="label label-primary" height="30px"><% out.print(connectMongoDB.getPopular_course()[8]);%></span>
                    <span class="label label-info" height="30px"><% out.print(connectMongoDB.getPopular_course()[9]);%></span>
                </div>
            </div>
            <p><%out.print(connectMongoDB.getLatest_date());%></p>

            <div id="piechart_3d" style="width: 900px; height: 500px;"></div>
            <div>
                <h4>Log</h4>
                <table>
                    <tr>
                        <td style="border-bottom: 1px solid #DED6D5;"> Client Request </td>
                        <td style="border-bottom: 1px solid #DED6D5;"> User Agent </td>
                        
                        <td style="border-bottom: 1px solid #DED6D5;"> Date </td>
                        <td style="border-bottom: 1px solid #DED6D5;"> Client Request URL</td>
                        <td style="border-bottom: 1px solid #DED6D5;"> Course Name</td>
                        <td style="border-bottom: 1px solid #DED6D5;"> Course Picture URL</td>
                        <td style="border-bottom: 1px solid #DED6D5;"> Latency</td>
                        <td style="border-bottom: 1px solid #DED6D5;"> Request to API</td>
                    </tr>
                    <%
                        for (int i = 0; i < connectMongoDB.getApi_url_array().size(); i++) {
                           
                    %>

                    <tr>
                        <td style="border-bottom: 1px solid #DED6D5  ;"> <%=connectMongoDB.getClient_request_array().get(i)%></td>
                        <td style="border-bottom: 1px solid #DED6D5;"><%=connectMongoDB.getUser_agent_array().get(i)%> </td>
                        
                        <td style="border-bottom: 1px solid #DED6D5;"> <%=connectMongoDB.getDate_array().get(i)%></td>
                        <td style="border-bottom: 1px solid #DED6D5;"><%=connectMongoDB.getClient_requesturl_array().get(i)%> </td>
                        <td style="border-bottom: 1px solid #DED6D5;"><%=connectMongoDB.getCourse_name_array().get(i)%> </td>
                        <td style="border-bottom: 1px solid #DED6D5;"><%=connectMongoDB.getCourse_pic_array().get(i)%> </td>
                        <td style="border-bottom: 1px solid #DED6D5;"> <%=connectMongoDB.getApi_url_array().get(i)%></td>
                        <td style="border-bottom: 1px solid #DED6D5;"> <%=connectMongoDB.getApi_request_array().get(i)%></td>
                    </tr>

                    <%
                        }
                    %>

                </table>
            </div>

        </div>
    </body>
    <script type="text/javascript">
        google.charts.load("current", {packages: ["corechart"]});
        google.charts.setOnLoadCallback(drawChartTime);
        function drawChartTime() {
            var monday =<%=connectMongoDB.getWeek()[0]%>;
            var tuesday =<%=connectMongoDB.getWeek()[1]%>;
            var wednesday =<%=connectMongoDB.getWeek()[2]%>;
            var thursday =<%=connectMongoDB.getWeek()[3]%>;
            var friday =<%=connectMongoDB.getWeek()[4]%>;
            var saturday =<%=connectMongoDB.getWeek()[5]%>;
            var sunday =<%=connectMongoDB.getWeek()[6]%>;
            var data = google.visualization.arrayToDataTable([
                ['Task', 'Hours per Day'],
                ['Monday', monday],
                ['Tuesday', tuesday],
                ['Wednesday', wednesday],
                ['Thursday', thursday],
                ['Friday', friday],
                ['Saturday', saturday],
                ['Sunday', sunday]
            ]);
            var options =
                    {
                        title: 'Vist Time Analysis',
                        is3D: true,
                    };

            var chart = new google.visualization.PieChart(document.getElementById('piechart_3d'));
            chart.draw(data, options);
        }
    </script>
</html>
