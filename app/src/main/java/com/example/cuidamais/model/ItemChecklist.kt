package com.example.cuidamais.model

// um item do checklist de adaptacao do ambiente
// ok = true quer dizer que aquilo ja esta seguro na casa
data class ItemChecklist(
    val id: Int,
    val comodo: String,
    val texto: String,
    val dica: String,
    val ok: Boolean = false
)


// lista inicial de itens, baseada nos exemplos do documento de visao
fun itensAmbientePadrao(): List<ItemChecklist> = listOf(
    ItemChecklist(1, "Sala e corredores", "Não há tapetes soltos", "Retire o tapete ou fixe com fita antiderrapante."),
    ItemChecklist(2, "Sala e corredores", "Fios e cabos fora do caminho", "Prenda os fios junto da parede pra ninguém tropeçar."),
    ItemChecklist(3, "Sala e corredores", "Ambiente bem iluminado", "Troque lâmpadas fracas e deixe luz nos corredores."),

    ItemChecklist(4, "Escada", "A escada tem corrimão", "Instale corrimão firme, de preferência dos dois lados."),
    ItemChecklist(5, "Escada", "Degraus com fita antiderrapante", "Cole fita antiderrapante na borda de cada degrau."),
    ItemChecklist(6, "Escada", "Interruptor de luz no início e no fim", "Garanta que dê pra acender a luz antes de subir ou descer."),

    ItemChecklist(7, "Banheiro", "Tem barra de apoio no box e perto do vaso", "Instale barras de apoio fixadas na parede, não use toalheiro."),
    ItemChecklist(8, "Banheiro", "Tapete antiderrapante dentro do box", "Use tapete com ventosas ou fita antiderrapante no piso."),
    ItemChecklist(9, "Banheiro", "Piso sempre seco, sem poças", "Seque o chão depois do banho e evite deixar água parada."),

    ItemChecklist(10, "Quarto", "Caminho livre da cama até o banheiro", "Tire móveis e objetos do trajeto, principalmente à noite."),
    ItemChecklist(11, "Quarto", "Luz ao alcance perto da cama", "Deixe um abajur ou luz de vigília ao alcance de quem dorme."),

    ItemChecklist(12, "Cozinha", "Itens do dia a dia ao alcance, sem banquinho", "Guarde panelas e copos em prateleiras na altura do peito.")
)