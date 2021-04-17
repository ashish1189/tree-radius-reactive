FROM adoptopenjdk/openjdk11:latest

#Image metadata
LABEL author = "Ashish Deshpande"
LABEL email = "ashishdeshpande123@gmail.com"
LABEL description = "NYC Tree census data. This service gets number of each type of trees \
					available in the given radius from point of interest represented in XY plain."

#Landing directory for app.jar
RUN mkdir /home/app

# Environment port
EXPOSE 9093

#Add app.jar from local to destination in container
ADD target/app.jar /home/app/app.jar

#Create softlink to make ap.jar executable from anywhere in container
RUN ln -s /home/app/app.jar /bin/app.jar

#Set working directory as bin
WORKDIR /bin

ENTRYPOINT ["java", "-jar", "app.jar"]