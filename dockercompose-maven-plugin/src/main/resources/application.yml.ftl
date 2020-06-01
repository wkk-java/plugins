version: '3'
services:
    ${projectName}:
        image: ${dockerNexusServerPrefix}${projectName}:${projectVersion}
        container_name: ${profile}-${projectName}
        deploy:
            restart_policy:
                condition: on-failure
                delay: 5s
                max_attempts: 3
                window: 120s
            resources:
                limits:
                    cpus: '0.1'
                    memory: 200M
                reservations:
                    cpus: '0.001'
                    memory: 64M
        ports:
            - ${outsidePort}:${insidePort}
        volumes:
            - /var/data/tmp:/tmp
            - /var/data/logs:/logs

