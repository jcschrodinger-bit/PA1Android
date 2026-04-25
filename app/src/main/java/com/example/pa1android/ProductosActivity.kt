package com.example.pa1android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pa1android.ui.theme.PA1AndroidTheme

data class Producto(val nombre: String, val precio: Int, val imagenRes: Int, val descripcion: String = "")

class ProductosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PA1AndroidTheme {
                var selectedFilter by remember { mutableStateOf("Botas") }
                // el estado ahora se sincroniza con CartManager
                
                // configurar launcher para recibir resultado de los detalle
                val detailLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
                    contract = androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    if (result.resultCode == android.app.Activity.RESULT_OK) {
                        val nombreProducto = result.data?.getStringExtra("NOMBRE")
                        val precio = result.data?.getIntExtra("PRECIO", 0) ?: 0
                        if (nombreProducto != null) {
                            CartManager.addProduct(nombreProducto, precio)
                        }
                    }
                }

                // Botas
                val listaBotas = listOf(
                    Producto("Sienne Étoile", 240, R.drawable.sienne_etoile, "Esta bota over-the-knee es la pieza definitiva para la mujer que desea una silueta infinita y una presencia magnética. Su diseño artesanal de textura aterciopelada captura la luz, logrando el equilibrio ideal entre la calidez estacional y el glamour de las pasarelas internacionales."),
                    Producto("Bordeaux Cavalière", 260, R.drawable.bordeaux_cavaliere, "Inspirada en la estética ecuestre de la alta sociedad, esta bota de grano entero redefine la elegancia funcional con un brillo sofisticado y duradero. Su diseño clásico transita sin esfuerzo hacia eventos de alto nivel, siendo ideal para quien busca nobleza en los materiales y una autoridad femenina impecable."),
                    Producto("Noir Impérial", 280, R.drawable.noir_imperial, "Una obra de arquitectura minimalista diseñada para la mujer vanguardista que entiende que el lujo reside en la perfección de la línea. Su tacón geométrico y puntera estilizada ofrecen una visión cosmopolita, convirtiéndola en el complemento iconográfico indispensable para proyectar éxito con una audacia silenciosa."),
                    Producto("Chocolat Velours Comfort", 220, R.drawable.chocolat_velours_comfort, "Este modelo representa el \"lujo silencioso\", fusionando una densidad textil superior con un forro que garantiza una calidez inigualable sin sacrificar la distinción. Es la opción definitiva para la mujer que exige el máximo confort y una estética de élite incluso en los climas más exigentes."),
                    Producto("Charbon Architech", 250, R.drawable.charbon_architech, "La fusión perfecta de ingeniería vanguardista y sofisticación, con una caña que se adapta como una segunda piel. Su tacón con acabado metálico añade un toque arquitectónico que eleva cualquier conjunto, garantizando una silueta impecable para la mujer con una visión de poder."),
                    Producto("Émeraude Chelsea", 270, R.drawable.emeraude_chelsea, "Una reinterpretación lujosa del diseño clásico, creada para quienes entienden que la distinción reside en la audacia de los materiales nobles. Con detalles satinados y un tacón de madera tratada, ofrece una sofisticación moderna y relajada para los entornos urbanos más exclusivos."),
                    Producto("Expresso Lacet", 230, R.drawable.expresso_lacet, "Esencia de elegancia artesanal que combina cuero graneado con un intrincado detalle de cordones grabados con láser. Su estructura equilibrada ofrece una estabilidad envidiable sin perder sofisticación, siendo el calzado que comunica exclusividad y un estilo atemporal hecho a medida."),
                    Producto("Nuit Velours Étoile", 290, R.drawable.nuit_velours_etoile, "El epítome de la elegancia nocturna, diseñada para ocasiones donde cada detalle cuenta y la pierna se envuelve como una joya. Su intrincado bordado de filigrana asegura que serás el centro de todas las miradas en eventos de alfombra roja o estrenos de ópera."),
                    Producto("Botte Exotique Caramel", 310, R.drawable.botte_exotique_caramel, "Definición de elegancia texturizada para la mujer que busca un lujo exótico y refinado en su calzado. Su patrón grabado y herrajes de distinción aportan una altura imponente y un aire aristocrático, convirtiéndola en una pieza indispensable de cualquier guardarropa de alto nivel."),
                    Producto("Botte Étoile de Minuit", 330, R.drawable.botte_etoile_de_minuit, "Una silueta estilizada que captura la esencia de la distinción nocturna mediante un acabado aterciopelado y bordados de alta costura. Diseñada para ofrecer un glamour absoluto, su perfil sofisticado es la elección predilecta para la mujer cosmopolita que no acepta menos que la perfección.")
                )

                // Zapatillas
                val listaZapatillas = listOf(
                    Producto("Terra Suede Runner", 180, R.drawable.terra_suede_runner, "Una zapatilla que redefine el lujo urbano, combinando gamuza de primera calidad con una suela ergonómica diseñada para el movimiento constante. Su tonalidad tierra evoca una conexión con lo natural sin perder la sofisticación cosmopolita."),
                    Producto("Aethel Minimalist", 195, R.drawable.aethel_minimalist, "La pureza del diseño escandinavo aplicada al calzado deportivo de élite. Con líneas limpias y una construcción monocromática, esta zapatilla es el complemento perfecto para un look 'athleisure' que respira exclusividad y modernidad."),
                    Producto("Aura Geometric Sculpt", 210, R.drawable.aura_geometric_sculpt, "Una pieza de arte para tus pies, donde la geometría se encuentra con el confort supremo. Su estructura esculpida no solo ofrece un soporte excepcional, sino que proyecta una imagen de vanguardia y diseño arquitectónico único."),
                    Producto("Filigree Lace Sneaker", 225, R.drawable.filigree_lace_dress_neaker, "La delicadeza de la alta costura fusionada con la versatilidad de una zapatilla. Sus detalles de encaje filigranado aportan una feminidad inesperada y lujosa, ideal para quienes buscan destacar con elegancia incluso en momentos informales."),
                    Producto("Mosaic Knit Premium", 200, R.drawable.mosaic_knit, "Una innovadora construcción de tejido multidimensional que ofrece una transpirabilidad superior y un estilo visual cautivador. Esta zapatilla representa la armonía entre la tecnología textil avanzada y una estética de mosaico artístico."),
                    Producto("Glide Sculpt Platinum", 215, R.drawable.glide_sculpt, "Fluidez y elegancia en cada línea. Este modelo presenta una suela esculpida que parece desafiar la gravedad, proporcionando una transición suave en la pisada y un perfil aerodinámico que personifica el dinamismo moderno."),
                    Producto("Vertex Hardware Urban", 240, R.drawable.vertex_hardware, "Para quienes buscan un estilo industrial y sofisticado. Esta zapatilla incorpora detalles de herrería fina y texturas técnicas, logrando un equilibrio perfecto entre la resistencia urbana y el lujo de alta gama."),
                    Producto("Vanguard Multi-Strap", 235, R.drawable.vanguard_multi_strap, "Una declaración de estilo audaz que reemplaza los cordones tradicionales por un sistema de múltiples correas de diseño. Su silueta vanguardista ofrece un ajuste personalizado y una presencia visual imponente en cualquier entorno."),
                    Producto("Tressé Élégance Luxe", 250, R.drawable.tresse_elegance, "Una obra de maestra de artesanía que incorpora un patrón de trenzado manual en cuero napa de la más alta calidad. Esta zapatilla fusiona la tradición artesanal con una silueta contemporánea, ideal para la mujer que valora el detalle y la exclusividad."),
                    Producto("Aero Luxe Runner", 260, R.drawable.aero_luxe_runner, "La cúspide de la ingeniería y el estilo. Esta zapatilla ultraligera cuenta con una cámara de aire oculta para un confort inigualable y acabados en seda técnica, diseñada para quienes exigen rendimiento deportivo con una estética de alta costura.")
                )

                // Tacos
                val listaTacos = listOf(
                    Producto("Oasis Brodé", 350, R.drawable.oasis_brode, "Un taco de ensueño que combina la delicadeza del bordado artesanal con una silueta estructuralmente perfecta. Su diseño evoca la frescura de un jardín secreto, ideal para ceremonias donde la elegancia y la feminidad deben brillar con luz propia."),
                    Producto("Lumière Gravée", 380, R.drawable.lumiere_gravee, "La personificación del lujo nocturno. Este zapato destaca por sus detalles grabados que capturan y reflejan la luz en cada paso, creando un efecto hipnótico. Es la elección definitiva para galas y eventos de alfombra roja."),
                    Producto("L'Architecture Nude", 320, R.drawable.larchitecture_nude, "Minimalismo y sofisticación se funden en este taco de tono piel. Su diseño arquitectónico estiliza la figura de manera natural, ofreciendo una elegancia discreta pero poderosa que complementa cualquier atuendo de alta costura."),
                    Producto("Plateforme Cachemire", 400, R.drawable.plateforme_cachemire, "La combinación perfecta de altura imponente y confort absoluto. Con una plataforma diseñada para la estabilidad y acabados en texturas suaves como el cachemir, este modelo es una declaración de poder y estilo sin concesiones."),
                    Producto("Noir Étincelant", 420, R.drawable.noir_etincelant, "El misterio del negro profundo se encuentra con un brillo estelar en este diseño vanguardista. Su acabado centelleante está pensado para la mujer que desea destacar en la oscuridad con una elegancia eléctrica y moderna."),
                    Producto("Dentelle de Nuit", 390, R.drawable.dentelle_de_nuit, "La seducción del encaje francés llevada al calzado de lujo. Este taco envuelve el pie en una trama de encaje delicado y resistente, creando un juego de transparencias que es puro arte y sofisticación nocturna."),
                    Producto("Python Graphique", 460, R.drawable.python_graphique, "Audacia y exclusividad se unen en este modelo con textura de pitón grabada. Su patrón gráfico natural aporta un carácter fuerte y aristocrático, ideal para quienes ven la moda como una expresión de liderazgo y distinción."),
                    Producto("Corail Doré Luxury", 375, R.drawable.corail_dore, "Una explosión de color y luz inspirada en los tesoros marinos. Este taco combina un tono coral vibrante con detalles en oro, creando una pieza de coleccionista que irradia calidez, lujo y una vitalidad inigualable."),
                    Producto("Jardin d'Or Premium", 430, R.drawable.jardin_d_or, "Una pieza maestra que fusiona la belleza orgánica de un jardín dorado con la precisión de la alta zapatería. Sus relieves en oro y diseño floral lo convierten en el calzado predilecto para eventos de gala diurnos o bodas de ensueño."),
                    Producto("Python de Cristal Luxe", 480, R.drawable.python_de_cristal, "La máxima expresión del lujo exótico. Este taco combina la textura salvaje del pitón con incrustaciones de micro-cristales que añaden un brillo multidimensional, diseñado para la mujer que no acepta nada menos que la perfección absoluta.")
                )

                // Sandalias
                val listaSandalias = listOf(
                    Producto("Sky Blue Geo Flat", 120, R.drawable.sky_blue_geo_flat, "La esencia de la libertad estival capturada en una sandalia plana de diseño geométrico. Su tono azul cielo evoca la serenidad de los días mediterráneos, mientras que su estructura minimalista ofrece un confort inigualable para caminatas elegantes junto al mar."),
                    Producto("Émeraude Cage Mule", 150, R.drawable.emeraude_cage_mule, "Una mule de diseño arquitectónico que envuelve el pie en una estructura de 'jaula' sofisticada. En un verde esmeralda profundo, esta pieza es el equilibrio perfecto entre la ventilación estival y la alta costura urbana."),
                    Producto("Tournesol Knot Block", 140, R.drawable.tournesol_knot_block, "Inspirada en la calidez del sol, esta sandalia destaca por su detalle de nudo artesanal y un tacón de bloque que garantiza estabilidad sin perder estilo. Es la compañera ideal para transiciones fluidas del brunch a eventos de tarde."),
                    Producto("Rouge Passion Wraparound", 160, R.drawable.rouge_passion_wraparound, "Sensualidad y diseño se encuentran en esta sandalia de tiras envolventes. Su rojo pasión y su silueta estilizada están diseñados para la mujer que desea dejar una huella de sofisticación y audacia en cada paso del verano."),
                    Producto("Fuchsia Pink Celtic Knot", 175, R.drawable.fuchsia_pink_celtic_knot, "Una explosión de color fucsia combinada con un intrincado nudo celta artesanal. Esta sandalia es una declaración de estilo vibrante y sofisticado, diseñada para la mujer que no teme ser el centro de atención."),
                    Producto("Turquoise Caribbean Star", 185, R.drawable.turquoise_caribbean_star, "Evoca la claridad de las aguas caribeñas con esta sandalia adornada con detalles estelares. Su diseño ligero y su tono turquesa la convierten en la joya perfecta para las noches tropicales más exclusivas."),
                    Producto("Vibrant Orange Woven Wedge", 165, R.drawable.vibrant_orange_woven_wedge, "La comodidad de la cuña se une a la maestría del tejido artesanal en un naranja vibrante. Una sandalia que proyecta energía y vitalidad, ideal para eventos de verano al aire libre."),
                    Producto("Orchid Purple Sculptural Heel", 195, R.drawable.orchid_purple_sculptural_heel, "Arte y moda convergen en este modelo con tacón esculpido en tono orquídea. Su silueta única y vanguardista redefine la elegancia moderna en el calzado abierto."),
                    Producto("Fuchsia Vanguard Plateforme", 210, R.drawable.fuchsia_vanguard_plateforme, "Una sandalia de plataforma que combina la audacia del fucsia con un diseño de vanguardia. Su estructura imponente y acabados de lujo la convierten en la pieza central de cualquier look estival de alto impacto."),
                    Producto("Étoile d'Azur Sculptée", 220, R.drawable.sky_blue_geo_flat, "Una obra de arte para los pies que captura la esencia del cielo azul en una silueta esculpida. (Pendiente: Renombrar 'etoile_'d_azur_sculptee.png' eliminando la comilla para usar su imagen real).")
                )

                Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                    Scaffold(
                        containerColor = Color.Transparent,
                        topBar = {
                            Column(modifier = Modifier.padding(top = 48.dp, bottom = 16.dp)) {
                                Text(
                                    text = "Elegance",
                                    style = MaterialTheme.typography.displayMedium,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                FilterRow(selectedFilter) { selectedFilter = it }
                            }
                        }
                    ) { innerPadding ->
                        AnimatedContent(
                            targetState = selectedFilter,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
                            },
                            label = "FilterTransition",
                            modifier = Modifier.padding(innerPadding)
                        ) { targetFilter ->
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(24.dp),
                                contentPadding = PaddingValues(bottom = 120.dp)
                            ) {
                                if (targetFilter == "Botas") {
                                    items(listaBotas.size) { index ->
                                        val producto = listaBotas[index]
                                        ProductCard(
                                            producto = producto,
                                            isInitiallyFavorite = CartManager.isSelected(producto.nombre),
                                            onFavoriteChanged = { CartManager.toggleProduct(producto.nombre, producto.precio) }
                                        ) {
                                            val intent = Intent(this@ProductosActivity, ProductoDetalleActivity::class.java).apply {
                                                putExtra("NOMBRE", producto.nombre)
                                                putExtra("PRECIO", producto.precio)
                                                putExtra("IMAGEN_RES", producto.imagenRes)
                                                putExtra("DESCRIPCION", producto.descripcion)
                                            }
                                            detailLauncher.launch(intent)
                                        }
                                    }
                                } else if (targetFilter == "Zapatillas") {
                                    items(listaZapatillas.size) { index ->
                                        val producto = listaZapatillas[index]
                                        ProductCard(
                                            producto = producto,
                                            isInitiallyFavorite = CartManager.isSelected(producto.nombre),
                                            onFavoriteChanged = { CartManager.toggleProduct(producto.nombre, producto.precio) }
                                        ) {
                                            val intent = Intent(this@ProductosActivity, ProductoDetalleActivity::class.java).apply {
                                                putExtra("NOMBRE", producto.nombre)
                                                putExtra("PRECIO", producto.precio)
                                                putExtra("IMAGEN_RES", producto.imagenRes)
                                                putExtra("DESCRIPCION", producto.descripcion)
                                            }
                                            detailLauncher.launch(intent)
                                        }
                                    }
                                } else if (targetFilter == "Tacos") {
                                    items(listaTacos.size) { index ->
                                        val producto = listaTacos[index]
                                        ProductCard(
                                            producto = producto,
                                            isInitiallyFavorite = CartManager.isSelected(producto.nombre),
                                            onFavoriteChanged = { CartManager.toggleProduct(producto.nombre, producto.precio) }
                                        ) {
                                            val intent = Intent(this@ProductosActivity, ProductoDetalleActivity::class.java).apply {
                                                putExtra("NOMBRE", producto.nombre)
                                                putExtra("PRECIO", producto.precio)
                                                putExtra("IMAGEN_RES", producto.imagenRes)
                                                putExtra("DESCRIPCION", producto.descripcion)
                                            }
                                            detailLauncher.launch(intent)
                                        }
                                    }
                                } else if (targetFilter == "Sandalias") {
                                    items(listaSandalias.size) { index ->
                                        val producto = listaSandalias[index]
                                        ProductCard(
                                            producto = producto,
                                            isInitiallyFavorite = CartManager.isSelected(producto.nombre),
                                            onFavoriteChanged = { CartManager.toggleProduct(producto.nombre, producto.precio) }
                                        ) {
                                            val intent = Intent(this@ProductosActivity, ProductoDetalleActivity::class.java).apply {
                                                putExtra("NOMBRE", producto.nombre)
                                                putExtra("PRECIO", producto.precio)
                                                putExtra("IMAGEN_RES", producto.imagenRes)
                                                putExtra("DESCRIPCION", producto.descripcion)
                                            }
                                            detailLauncher.launch(intent)
                                        }
                                    }
                                } else {
                                    // Espacios reservados para las otras categorías
                                    items(10) { index ->
                                        val dummyProducto = Producto("Elegance $targetFilter ${index + 1}", 150 + (index * 10), R.drawable.img_zapato)
                                        ProductCard(
                                            producto = dummyProducto,
                                            isInitiallyFavorite = CartManager.isSelected(dummyProducto.nombre),
                                            onFavoriteChanged = { CartManager.toggleProduct(dummyProducto.nombre, dummyProducto.precio) }
                                        ) {
                                            val intent = Intent(this@ProductosActivity, ProductoDetalleActivity::class.java).apply {
                                                putExtra("NOMBRE", dummyProducto.nombre)
                                                putExtra("PRECIO", dummyProducto.precio)
                                                putExtra("IMAGEN_RES", dummyProducto.imagenRes)
                                            }
                                            detailLauncher.launch(intent)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                        FloatingNavBar(currentScreen = "Productos", onNavigate = { target ->
                            when(target) {
                                "Home" -> startActivity(Intent(this@ProductosActivity, MainActivity::class.java))
                                "Compras" -> startActivity(Intent(this@ProductosActivity, ComprasActivity::class.java))
                                "Profile" -> startActivity(Intent(this@ProductosActivity, PerfilActivity::class.java))
                            }
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun FilterRow(selectedFilter: String, onFilterSelected: (String) -> Unit) {
    val filters = listOf("Botas", "Zapatillas", "Tacos", "Sandalias")
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(filters) { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = { Text(filter) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color.Black,
                    selectedLabelColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    labelColor = Color.Black
                ),
                border = null,
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@Composable
fun ProductCard(
    producto: Producto,
    isInitiallyFavorite: Boolean = false,
    onFavoriteChanged: (Boolean) -> Unit = {},
    onClick: () -> Unit
) {
    var isFavorite by remember(isInitiallyFavorite) { mutableStateOf(isInitiallyFavorite) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.85f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(id = producto.imagenRes),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp)
                    .clip(RoundedCornerShape(18.dp)),
                contentScale = ContentScale.Crop
            )

            IconButton(
                onClick = { 
                    isFavorite = !isFavorite
                    onFavoriteChanged(isFavorite)
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .background(Color.White.copy(alpha = 0.5f), CircleShape)
                    .size(30.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (isFavorite) Color.Red else Color.Black,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = producto.nombre,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 4.dp),
            maxLines = 1
        )
        
        if (producto.precio > 0) {
            Text(
                text = "S/ ${producto.precio}.00",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}
