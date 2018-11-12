# Package Dependency Metrics



### R Commands

__Load data from csv file__
> hadoop <- read.csv(file="~/Desktop/PackageDependencyMetrics/data/hadoop.csv", header=TRUE, sep=",")

__Load graph library__
> library(ggplot2)

__Plot graph__
> ggplot(hadoop, aes(x=I, y=A, label=id)) +#
    geom_point(shape=1) + geom_text(aes(label=id), hjust=1.4, vjust=1.4, size = 3)

