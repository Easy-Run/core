# 1.0
core/
├── README.md
├── build.gradle (ou pom.xml)
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── easyrun/
│   │   │           ├── commands/          // Commandes globales (compile, adb, git, etc.)
│   │   │           │   ├── CompileCommand.java
│   │   │           │   ├── AdbCommand.java
│   │   │           │   └── ... 
│   │   │           ├── database/          // Module de gestion de la base de données (SQLite)
│   │   │           │   ├── DatabaseService.java
│   │   │           │   ├── ORM.java
│   │   │           │   └── ... 
│   │   │           ├── libraries/         // Wrappers et gestion des librairies tierces (scrcpy, JRE, etc.)
│   │   │           │   ├── ScrcpyWrapper.java
│   │   │           │   ├── JreManager.java
│   │   │           │   └── ... 
│   │   │           ├── config/            // Gestion de la configuration (si nécessaire)
│   │   │           │   └── ConfigService.java
│   │   │           └── utils/             // Outils généraux (gestion des processus, logs, etc.)
│   │   │               └── ProcessLauncher.java
│   │   └── resources/
│   │       └── application.properties    // Fichier(s) de config ou de ressources
│   └── test/
│       └── java/
│           └── com/
│               └── easyrun/
│                   └── ...               // Tests unitaires pour chaque module
└── docs/                                 // Documentation interne sur l'architecture, l'API, etc.
    └── architecture.md
