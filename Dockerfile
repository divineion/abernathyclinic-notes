# must begin with a FROM instruction
FROM eclipse-temurin:24-jre

# static instructions higher in the order
LABEL description="Backend service for doctors notes management"
WORKDIR /app
EXPOSE 8083

# # Copie du fichier le plus tard possible : en cas de modif, docker n'aura pas besoin de refaire les étapes précédentes'
COPY ./target/abernathyclinic-notes-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar" ]