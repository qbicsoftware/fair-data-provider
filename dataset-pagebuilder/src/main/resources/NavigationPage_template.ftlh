<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Dataset Navigation page</title>
        <style>
            * {
                margin: 20px;
            }
            header {
                border-radius: 25px;
                background-color: lightgrey;
                padding: 5px;
                text-align: left;
                display: flex;
            }
            table {
                border-collapse: collapse;
                width: 100%;
            }
            th, td {
                padding: 8px;
                text-align: left;
                border-bottom: 1px solid #ddd;
            }
            .arrow {
              border: solid black;
              border-width: 0 3px 3px 0;
              display: inline-block;
              padding: 3px;
              transform: rotate(135deg);
              -webkit-transform: rotate(135deg);
            }

        </style>
    </head>
    <body>
        <header>
            <a href="https://fair.qbic.uni-tuebingen.de"><i class="arrow left"></i></a>
            <h1>Datasets</h1>
        </header>
        <p>Here you can find all datasets available on this site.</p>
        <p>Note that this website is part of a proof of concept implementation for a bachelor thesis <br>
        and therefore, all datasets at this point are testing datasets only</p>
        <table>
            <tr>
                <th>Dataset identifier</th>
                <th>Description</th>
            </tr>
        <#list ids as id>
            <tr>
                <td> <a href=${urls[id?index]}> ${id} </a> </td>
                <td> ${descriptions[id?index]!"no description available"} </td>
            </tr>
        </#list>
        </table>
        </div>
    </body>
</html>

