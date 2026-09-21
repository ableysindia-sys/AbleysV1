package com.ableys.app.data.content

/**
 * What a family can use instead, from what is already in the house.
 *
 * Most of this catalogue's equipment-gated content assumes the equipment. For a family deciding
 * whether this app is worth keeping, an activity that opens with a product they do not own reads
 * as an advert, and the activity never gets done. Equipment should be the better version of
 * something already possible, not the price of entry.
 *
 * Substitutions are the ones practitioners actually suggest, in terms an Indian household will
 * recognise -- atta dough rather than "modelling compound", a dupatta rather than "a length of
 * fabric". Anything with a real safety cost is either written with the boundary attached or left
 * out: there is no bedsheet hammock here, because a swing that fails is a different category of
 * problem from an activity that is merely less good.
 */
object HouseholdAlternatives {

    /**
     * Matched against the equipment name in order, first hit wins. Keys are lowercase fragments.
     */
    private val byEquipment: List<Pair<String, String>> = listOf(
        "platform swing" to
            "No swing at home? Sit together on a pile of cushions and rock slowly side to side, " +
            "or rock gently in a sturdy chair. Do not hang anything from a ceiling fan hook or a " +
            "curtain rod — neither is built to hold a child.",
        "glider swing" to
            "Rock slowly side to side on a cushion pile instead, counting the same rhythm.",
        "t-swing" to
            "Rock slowly on a cushion pile instead, or sit back to back and sway together.",
        "tyre tube swing" to
            "Sit on a firm cushion and rock in slow circles instead.",
        "weighted lap pad" to
            "A folded quilt across the lap works, or a kilo of rice or dal sealed in a zip bag " +
            "inside a pillowcase. Across the thighs only, never the chest or tummy.",
        "weighted blanket" to
            "A folded cotton quilt gives some of the same weight. Tuck it over the legs, not the " +
            "face or chest.",
        "putty" to
            "Stiff atta dough is the usual stand-in and works well. Keep a separate ball for play " +
            "so nobody is squeezing tonight's roti.",
        "stepping stone" to
            "Lay out cushions, floor tiles marked with masking tape, or folded dupattas as the " +
            "stones. Anything that does not slide.",
        "balance beam" to
            "A strip of masking tape on the floor, or a dupatta laid flat in a straight line.",
        "scooter board" to
            "On a smooth floor, a child can lie on a large steel tray or a folded bedsheet while " +
            "an adult pulls slowly. Clear the path first and keep hands away from the edges.",
        "foam therapy mat" to
            "A folded quilt or a rolled yoga mat gives a soft surface for the same activity.",
        "foam mat" to
            "A folded quilt or a rolled yoga mat works for the same activity.",
        "wedge" to
            "A firm cushion folded under the chest gives a similar slope for short periods.",
        "body sock" to
            "A stretchy bedsheet held taut by an adult lets a child push against it. Keep it away " +
            "from the head and stop the moment they want out.",
        "sensory mat" to
            "Cut squares of different fabrics from what you have — silk, wool, jute, a towel — " +
            "and lay them in a path.",
        "visual timer" to
            "The phone timer works, laid face up where the child can see the numbers falling.",
        "pencil grip" to
            "Wrap a rubber band a few times around the pencil where the fingers sit, or thread the " +
            "pencil through a small hair clip.",
        "balance beams" to
            "A line of masking tape on the floor does the same job.",
        "play tent" to
            "Drape a bedsheet over two chairs, or clear the space under a bed or a dining table.",
        "pea pod" to
            "A beanbag, or a nest of cushions with a quilt over the top.",
        "squeeze ball" to
            "A ball of atta dough, a rolled sock, or a small cushion held in one hand.",
        "stress ball" to
            "A rolled pair of socks, or a ball of atta dough.",
        "putty set" to
            "Stiff atta dough, kept as a separate play ball.",
        "compression vest" to
            "A snug cotton vest worn under clothes gives a little of the same feeling.",
        "buckle" to
            "An old belt, a school bag with buckles, or a shirt with big buttons.",
        "adaptive" to
            "Any loose shirt with big buttons or a wide neck opening works for practising.",
        "loose t-shirt" to
            "Any loose cotton t-shirt already in the cupboard, ideally one size up.",
        "foam square" to
            "Folded dupattas, cushions, or floor tiles marked out with masking tape.",
        "foam pad" to
            "Folded dupattas or cushions laid flat, anything that does not slide on the floor.",
        "foam strip" to
            "A folded dupatta or a rolled towel laid in a line.",
        "playground ball" to
            "Any ball already in the house, or a pair of socks rolled tight for indoor throwing.",
        "textured material" to
            "Squares cut from what you have \u2014 silk, jute, a towel, a woollen shawl, bubble wrap.",
        "non-slip mat" to
            "A yoga mat cut into squares, or folded dupattas on a floor that does not slide.",
        // Generic "lap pad" after the weighted one, so the specific match wins where it applies.
        "lap pad" to
            "A folded quilt across the lap, or a kilo of rice sealed in a zip bag inside a " +
            "pillowcase. Across the thighs only, never the chest or tummy.",
        "floor tape" to
            "Masking tape, or folded dupattas laid out to mark the path.",
        "lidded jar" to
            "Whatever is already in the kitchen: a dabba with a screw lid, a shirt with buttons, " +
            "a fork. Nothing needs buying for this one.",
        "shirt with buttons" to
            "Any shirt in the cupboard with buttons, and a dabba with a screw lid."
    )

    /** Substitution for a named piece of equipment, or null when none is honest. */
    fun forEquipment(equipmentName: String?): String? {
        if (equipmentName.isNullOrBlank()) return null
        val n = equipmentName.lowercase()
        return byEquipment.firstOrNull { (key, _) -> n.contains(key) }?.second
    }

    /**
     * Equipment that needs drilling, a ceiling hook or a permanent fixture.
     *
     * Most Indian rentals are concrete slab, and a landlord's permission is a real obstacle
     * rather than a formality. Content built on these needs its alternative shown at the same
     * level as the product, not buried under it.
     */
    fun needsInstallation(equipmentName: String?): Boolean {
        if (equipmentName.isNullOrBlank()) return false
        val n = equipmentName.lowercase()
        return listOf("platform swing", "glider swing", "t-swing", "tyre tube swing", "climb net")
            .any { n.contains(it) }
    }
}
