<#ftl output_format="HTML">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <#if name??><title>QBiC dataset ${name}</title></#if>
    <meta name="description" content="This is a dataset landing page for the test dataset ${name}">
    <style>
        * {
            margin: 20px;
        }
        header {
            border-radius: 25px;
            background-color: lightgrey;
            padding: 15px;
            text-align: left;
        }
        main {
            border-radius: 25px;
            background-color: lightgrey;
            padding: 15px;
        }
        footer button{
            background-color: lightgrey;
        }
        table {
          border-collapse: collapse;
          border-style: hidden;
        }
        td {
            width: 15%;
            border: 1px solid black;
            padding: 5px;
        }
    </style>
</head>
<body>
    <header>
        <h3>Dataset ${name!"no name available"}</h3>
   </header>
   <main>
   <h4>Details</h4>
   <table>
        <tr>
            <td>Description</td>
            <td>${description!"sadly there is no information available"}</td>
        </tr>
        <tr>
            <td>Keywords</td>
            <td>${keywords!"sadly there is no information available"}</td>
        </tr>
        <tr>
            <td>Unique identifier</td>
            <td>${identifier}</td>
        </tr>
        <tr>
            <td>Created on</td>
            <td>${dateCreated!"sadly there is no information available"}</td>
        </tr>
        <tr>
            <td>Creator</td>
            <td>${creator!"sadly there is no information available"}</td>
        </tr>
        <tr>
            <td>Provider</td>
            <td>${provider.name}</td>
        </tr>
        <tr>
             <td>Measurement technique</td>
             <td>${measurementTechnique!"sadly there is no information available"}</td>
        </tr>
   </table>
   <h4>Further information</h4>
   <p>The license for this dataset is ${license!"sadly not available right now"}</p>
   <p>If you are interested in using this dataset or need any further information, <br> please do not hesitate to contact us!</p>
   <p>Further information about us and how to get in contact, you can find, <a href="https://portal.qbic.uni-tuebingen.de/portal/web/qbic/get-in-touch">here</a></p>
   </main>
   <footer>
        <a href="https://fair.qbic.uni-tuebingen.de/datasets/"><button>See more datasets</button></a>
   </footer>
<script type="application/ld+json">
    <#outputformat "JSON">
        ${markup}
    </#outputformat>
</script>
</body>
</html>

