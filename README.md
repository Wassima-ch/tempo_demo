# 🕵️ Traçage Distribué avec Spring Boot 3 & Grafana Tempo

![Java](https://img.shields.io/badge/Java-17+-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0+-green)
![Docker](https://img.shields.io/badge/Docker-Compose-blue)
![Grafana Tempo](https://img.shields.io/badge/Grafana-Tempo-yellow)

## 📖 Aperçu

Ce projet montre un **système complet de traçage distribué** utilisant des microservices.  
Il résout le problème de la "Boîte Noire" dans les architectures distribuées en permettant de suivre une requête HTTP unique à travers plusieurs services.

Nous utilisons **OpenTelemetry** pour injecter automatiquement des Trace IDs uniques et **Grafana Tempo** pour visualiser le cycle de vie des requêtes, la latence et les erreurs en temps réel.
---

## 🏗️ Architecture
Le projet contient 5 conteneurs Docker :
1. **Order Service (8081) :** Point d’entrée pour l’utilisateur.  
2. **Inventory Service (8082) :** Vérifie la disponibilité du stock.  
3. **Payment Service (8083) :** Traite les paiements (simule latence et erreurs).  
4. **Grafana Tempo :** Base de données pour stocker les traces.  
5. **Grafana :** Dashboard pour visualiser les graphiques en cascade (Waterfall).
---

## 🛠️ Prérequis
* **Java 17+** installé  
* **Maven** installé  
* **Docker Desktop** installé et en fonctionnement
---

## 🚀 Étape 1 : Construire le projet
Dans chaque dossier de service, construisez le JAR avec Maven :
```bash
# 1. Build Order Service
cd order-service
mvn clean package -DskipTests

# 2. Build Inventory Service
cd ../inventory-service
mvn clean package -DskipTests

# 3. Build Payment Service
cd ../payment-service
mvn clean package -DskipTests

# Retour au dossier racine
cd ..
```
---

## 🐳 Étape 2: Lancer l’infrastructure
Utilisez Docker Compose pour démarrer tous les services, Tempo et Grafana simultanément :
```Bash
# Démarrer tous les services en arrière-plan
docker-compose up -d
# Vérifier que les 5 conteneurs sont bien lancés
docker ps
```
---

## 🧪 Étape 3 : Tester les scénarios
Nous avons créé une logique spécifique pour simuler des problèmes réels. Utilisez ces commandes pour déclencher les traces.
#### 🟢 Scenario 1: Le chemin idéal (Happy Path)
Requête normale et réussie :
```Bash
curl http://localhost:8081/checkout/1
```
* **Résultat:** Retourne IN_STOCK & PAYMENT_SUCCESSFUL 
* **Grafana Tempo:** le graphique en cascade (Waterfall graph) est parfait. La durée totale est d'environ 503ms. On voit clairement l'Order Service appeler l'Inventory, puis le Payment. Tout est vert et court .

#### 🟡 Scenario 2: Le chemin lent (Slow Path)
Simulons un problème de performance:
```Bash
curl http://localhost:8081/checkout/99
```
* **Résultat:** Le terminal reste bloqué 5 secondes
* **Grafana Tempo:** la différence est flagrante. La durée totale est de 5,1 secondes. La barre de l'Order Service s'étire sur tout l'écran, mais on voit en bas que c'est la barre du Payment Service qui est immense (5,03s). Je peux instantanément prouver que le goulot 

#### 🔴 Scenario 3: Le chemin d'erreur (Error Path)
Simule une défaillance système (Exception) :
```Bash
curl http://localhost:8081/checkout/0
```
* **Résultat:** Retourne 500 Internal Server Error
* **Grafana Tempo:** , je cherche la trace avec les icônes de point d'exclamation rouges (!). On voit des icônes d'erreur devant l'Order Service ET le Payment Service. Le Payment Service a échoué, l'erreur est remontée jusqu'à l'Order Service, provoquant l'échec global. Tempo a parfaitement capturé le crash. 
---

## 📊 Étape 4: Visualiser dans Grafana
Ouvrez votre navigateur : http://localhost:3001

Allez dans Explore

Sélectionnez Tempo dans le menu déroulant

Cliquez sur Run Query (ou collez un Trace ID depuis vos logs)

Cliquez sur une trace pour voir le Waterfall Graph

### 🛑 Arrêter le projet
Pour arrêter et supprimer les conteneurs :
```Bash
docker-compose down
```
