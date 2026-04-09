<?php

/* Définit les urls des APIs, getenv récupère les variables d'env */
$cssPath = '../include/style.css';
$apiDishes = getenv('JAVA_API_DISHES_URL') ?: 'http://localhost:3003/dishes';
$apiUsers = getenv('JAVA_API_USERS_URL') ?: 'http://localhost:3003/users';
$apiMenus = getenv('JAVA_API_MENUS_URL') ?: 'http://localhost:3004/menus';
$apiOrders = getenv('JAVA_API_ORDERS_URL') ?: 'http://localhost:3005/orders';

/* Récupère le json d'une API */
function fetchJson(string $url): ?array
{
	$ch = curl_init($url);
	if ($ch === false) {
		return null;
	}

	curl_setopt_array($ch, [
		CURLOPT_RETURNTRANSFER => true,
		CURLOPT_TIMEOUT => 3,
		CURLOPT_HTTPHEADER => ['Accept: application/json'],
        /* Suit les redirections et limite à 5 seulement */
		CURLOPT_FOLLOWLOCATION => true,
		CURLOPT_MAXREDIRS => 5,
	]);

	$content = curl_exec($ch);
	$statusCode = (int) curl_getinfo($ch, CURLINFO_HTTP_CODE);
	$hasError = curl_errno($ch) !== 0;

    /* S'il y a une erreur retourne null */
	if ($hasError || $content === false || $statusCode < 200 || $statusCode >= 300) {
		return null;
	}

    /* Décode le json et le retourne comme array */
	$decoded = json_decode($content, true);
	return is_array($decoded) ? $decoded : null;
}

/* Aucune réelle fonction autre que de l'esthétique (Formatage des dates) */
function formatDate(?string $date, string $format = 'd/m/Y'): string
{
    if (empty($date)) {
        return 'Date inconnue';
    }

    try {
        return (new DateTime($date))->format($format);
    } catch (Exception $e) {
        return 'Date inconnue';
    }
}

/* Récupère les APIs avec la fonction plus haut */
$dishesData = fetchJson($apiDishes);
$usersData = fetchJson($apiUsers);
$menusData = fetchJson($apiMenus);
$ordersData = fetchJson($apiOrders);

/*
Vérifie si les données existent
Oui --> Boucle pour afficher les infos sous forme de liste
Non --> Affiche "Aucune donnée"
Opérateur ?? pour afficher une valeur par défaut au cas où
Valeurs séparées par des '.' pour les afficher à la suite
*/
?>
<!DOCTYPE html>
<html lang="fr">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Super Duper Eater Simulator</title>
	<link rel="stylesheet" href="<?= $cssPath?>">
</head>
<body>
	<main class="container">
		<section>
			<div>
				<h1>Plats</h1>
                <h3>Lien de l'api : <?= $apiDishes?></h3>
				<?php if (empty($dishesData)): ?>
					<p>Aucune donnée</p>
				<?php else: ?>
					<ul>
						<?php foreach ($dishesData as $item): ?>
							<li><?= 'ID : ' . ($item['id'] ?? '?') . ' - ' . ($item['name'] ?? 'Plat sans nom') . ' - ' . ($item['price'] ?? 'Plat sans prix') . '€' ?></li>
                            <p class="italic-desc"><?= $item['description'] ?? 'Ce plat ne possède pas de description.'?></p>
						<?php endforeach; ?>
					</ul>
				<?php endif; ?>
			</div>

            <div>
                <h1>Utilisateurs</h1>
                <h3>Lien de l'api : <?= $apiUsers?></h3>
                <?php if (empty($usersData)): ?>
                    <p>Aucune donnée</p>
                <?php else: ?>
                    <ul>
						<?php foreach ($usersData as $item): ?>
							<li><?= 'ID : ' . ($item['id'] ?? '?') . ' - ' . ($item['lastName'] ?? '"Utilisateur sans nom" ') . ' ' . ($item['firstName'] ?? '"Utilisateur sans prénom" ')?></li>
                            <p class="italic-desc"><?= 'Adresse mail : ' . $item['email'] ?? "Aucune adresse mail renseignée."?></p>
                            <p class="italic-desc"><?= 'Adresse : ' . $item['address'] ?? "Aucune adresse renseignée."?></p>
                        <?php endforeach; ?>
                    </ul>
                <?php endif; ?>
            </div>

			<div>
				<h1>Menus</h1>
				<h3>Lien de l'api : <?= $apiMenus?></h3>
				<?php if (empty($menusData)): ?>
					<p>Aucune donnée</p>
				<?php else: ?>
					<ul>
						<?php foreach ($menusData as $item): ?>
							<li><?= 'ID : ' . ($item['id'] ?? '?') . ' - ' . ($item['name'] ?? 'Menu sans nom') ?></li>
							<p class="italic-desc"><?= 'Créateur : ' . ($item['creatorName'] ?? 'Inconnu') . ' (ID ' . ($item['creatorId'] ?? '?') . ')' ?></p>
							<p class="italic-desc"><?= 'Date de création : ' . formatDate($item['creationDate'] ?? 'Inconnue') ?></p>
							<p class="italic-desc"><?= 'Date de mise à jour : ' . formatDate($item['updateDate'] ?? 'Inconnue') ?></p>
							<?php $dishes = (isset($item['dishes']) && is_array($item['dishes'])) ? $item['dishes'] : []; ?>
							<?php if (empty($dishes)): ?>
								<p class="italic-desc">Aucun plat trouvé dans ce menu.</p>
							<?php else: ?>
								<p class="italic-desc">Plats :</p>
								<?php foreach ($dishes as $dish): ?>
									<p class="italic-desc"><?= '- ID ' . ($dish['id'] ?? '?') . ' - ' . ($dish['name'] ?? 'Plat sans nom') . ' - ' . ($dish['price'] ?? 'Plat sans prix') . '€' ?></p>
								<?php endforeach; ?>
							<?php endif; ?>
							<p class="italic-desc"><?= 'Prix total : ' . ($item['totalPrice'] ?? 'Inconnu') . '€' ?></p>
						<?php endforeach; ?>
					</ul>
				<?php endif; ?>
			</div>

			<div>
				<h1>Commandes</h1>
				<h3>Lien de l'api : <?= $apiOrders?></h3>
				<?php if (empty($ordersData)): ?>
					<p>Aucune donnée</p>
				<?php else: ?>
					<ul>
						<?php foreach ($ordersData as $item): ?>
							<li><?= 'ID : ' . ($item['id'] ?? '?') . ' - Client ID : ' . ($item['subscriberId'] ?? '?') ?></li>
							<p class="italic-desc"><?= 'Date de commande : ' . formatDate($item['orderDate'] ?? null) ?></p>
							<p class="italic-desc"><?= 'Date de livraison : ' . formatDate($item['deliveryDate'] ?? null) ?></p>
							<p class="italic-desc"><?= 'Adresse de livraison : ' . ($item['deliveryAddress'] ?? 'Non renseignée') ?></p>
							<?php $items = (isset($item['items']) && is_array($item['items'])) ? $item['items'] : []; ?>
							<?php if (empty($items)): ?>
								<p class="italic-desc">Aucun article trouvé dans cette commande.</p>
							<?php else: ?>
								<p class="italic-desc">Articles :</p>
								<?php foreach ($items as $lineItem): ?>
									<p class="italic-desc"><?= '- ' . ($lineItem['menuName'] ?? 'Menu sans nom') . ' (ID ' . ($lineItem['menuId'] ?? '?') . ') x' . ($lineItem['quantity'] ?? '?') . ' - ' . ($lineItem['unitPrice'] ?? 'Prix inconnu') . '€/u - Total : ' . ($lineItem['linePrice'] ?? 'Inconnu') . '€' ?></p>
								<?php endforeach; ?>
							<?php endif; ?>
							<p class="italic-desc"><?= 'Prix total : ' . ($item['totalPrice'] ?? 'Inconnu') . '€' ?></p>
                        <?php endforeach; ?>
                    </ul>
                <?php endif; ?>
            </div>
        </section>
    </main>
</body>
</html>
