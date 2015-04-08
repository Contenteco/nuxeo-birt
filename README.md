# Nuxeo BIRT

This addon contains a set of Nuxeo Plugins to integrate BIRT Reporting engine into Nuxeo.

* BIRT report files are imported as Nuxeo Documents (BIRT Report Models)
* Reports can be rendered from Nuxeo Documents (BIRT Report Instances)
* Reports instances are bound to Document Context
* Report Rendering is automatically adjusted to use Nuxeo server data sources

# Building

    mvn clean install

## Deploying

Install [the Nuxeo - BIRT Integration (Reporting) Marketplace Package](https://connect.nuxeo.com/nuxeo/site/marketplace/package/nuxeo-birt-integration).
Or manually copy the built artifacts into `$NUXEO_HOME/templates/custom/bundles/` and activate the "custom" template.

## QA results

[![Build Status](https://qa.nuxeo.org/jenkins/buildStatus/icon?job=addons_nuxeo-birt-master)](https://qa.nuxeo.org/jenkins/job/addons_nuxeo-birt-master/)

# About Nuxeo

Nuxeo dramatically improves how content-based applications are built, managed and deployed, making customers more agile, innovative and successful. Nuxeo provides a next generation, enterprise ready platform for building traditional and cutting-edge content oriented applications. Combining a powerful application development environment with SaaS-based tools and a modular architecture, the Nuxeo Platform and Products provide clear business value to some of the most recognizable brands including Verizon, Electronic Arts, Netflix, Sharp, FICO, the U.S. Navy, and Boeing. Nuxeo is headquartered in New York and Paris. More information is available at www.nuxeo.com.
