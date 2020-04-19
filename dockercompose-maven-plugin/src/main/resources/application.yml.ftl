version: '3'
services:
    ${projectName}:
        restart: always
        image: ${profile}-${projectName}:${projectVersion}
        container_name: ${profile}-${projectName}
        ports:
            - ${outsidePort}:${insidePort}
        volumes:
            - var/data/tmp:/tmp