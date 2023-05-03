<!DOCTYPE html>
<html lang="en">
    <head>
      <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
      <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.3/dist/jquery.slim.min.js"></script>
      <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
      <meta charset="UTF-8">
      <meta http-equiv="X-UA-Compatible" content="IE=edge">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>QBiC dataset ${name}</title>
      <meta name="description" content="Dataset landing page for the test dataset ${name}">
    </head>
    <body>
        <main>
          <div class="card mx-auto mt-2" style="min-width: 18rem; max-width: 80%">
            <div class="card-body">
              <div id="badges container">
                <div class="row justify-content-end">
                    <#list keywords as key>
                        <div class="badge badge-pill badge-info mr-2 p-3">${key}</div>
                    </#list>
                </div>
              </div>
              <h3 class="card-title">Dataset ${name}</h3>
              <h6 class="card-subtitle text-muted">
                ${identifier}
              </h6>
              <p class="card-text mt-3">
                ${description}
            </div>
            <div class="card-footer container" style="min-width: 18rem; max-width: 100%">
              <div class="row">
                <div class="col text-left">
                  Created by: ${creator.name! "currently no information available"}
                </div>
                <div class="col text-center">Provided by: ${publisher.name! "currently no information available"}</div>
                <div class="col text-right">
                  ${dateCreated! "currently no information available"}
                </div>
              </div>
            </div>
            <div class="card-footer container" style="min-width: 18rem; max-width: 100%">
              <div class="row">
                <div class="col-md-auto">
                    Licensed under <a href="${license}">${license}</a>
                </div>
                <div class="col text-right">
                    <a href="https://portal.qbic.uni-tuebingen.de/portal/web/qbic/get-in-touch">
                        <button type="button" class="btn btn-outline-dark">Get in contact with us!</button>
                    </a>
                </div>
              </div>
            </div>
          </div>
        </main>
        <footer class="card-footer container border-0 bg-white" style="min-width: 18rem; max-width: 80%">
            <a href="https://fair.qbic.uni-tuebingen.de/datasets/">
                <button type="button" class="btn btn-outline-info mt-2">See more datasets</button>
            </a>
        </footer>
        <script type="application/ld+json">
            <#outputformat "JSON">
                    ${markup}
            </#outputformat>
        </script>
    </body>
</html>
