package com.resq.screen

import com.resq.*
import com.resq.data.*
import com.resq.ui.*

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController

private fun L(vm: Vm, en: String, it: String) = if (vm.lang == "IT") it else en

@Composable
private fun Qa(q: String, a: String) {
    Pane {
        Text(q, fontWeight = FontWeight.ExtraBold, color = Ink, fontSize = 15.sp)
        Spacer(Modifier.height(6.dp))
        Text(a, color = Sub, fontWeight = FontWeight.SemiBold)
    }
    Spacer(Modifier.height(12.dp))
}

@Composable
private fun Para(t: String) {
    Text(t, color = Ink, fontWeight = FontWeight.SemiBold)
    Spacer(Modifier.height(12.dp))
}

@Composable
private fun Step(t: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
        Text("•  ", color = Grn, fontWeight = FontWeight.ExtraBold)
        Text(t, color = Ink, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun Faq(vm: Vm, nav: NavHostController) {
    Scaf(vm, nav) {
        Hero(tr(vm, "Help & FAQ"), L(vm, "Answers to common questions.", "Risposte alle domande frequenti."), gAi)
        Spacer(Modifier.height(16.dp))
        Qa(L(vm, "Is ResQShare free?", "ResQShare è gratis?"),
            L(vm, "Yes, completely free for everyone — donors and recipients alike.",
                "Sì, completamente gratis per tutti — sia donatori che beneficiari."))
        Qa(L(vm, "How do I donate?", "Come faccio a donare?"),
            L(vm, "Tap Create, add a photo and details, and our AI writes a clean, safe listing for you.",
                "Tocca Crea, aggiungi una foto e i dettagli, e la nostra IA scrive per te un annuncio chiaro e sicuro."))
        Qa(L(vm, "How do people reach me?", "Come mi contattano le persone?"),
            L(vm, "Through the built-in chat. Your exact address is never shown — only your area.",
                "Tramite la chat integrata. Il tuo indirizzo esatto non viene mai mostrato — solo la tua zona."))
        Qa(L(vm, "Is the food safe?", "Il cibo è sicuro?"),
            L(vm, "The AI flags risk and gives a safety checklist, but final safety is the donor's responsibility before handover.",
                "L'IA segnala i rischi e fornisce una checklist di sicurezza, ma la sicurezza finale è responsabilità del donatore prima della consegna."))
        Qa(L(vm, "What is the pickup radius?", "Cos'è il raggio di ritiro?"),
            L(vm, "It is the distance around your point where people can collect the item, or how far you are willing to travel.",
                "È la distanza intorno al tuo punto entro cui le persone possono ritirare l'oggetto, o fin dove sei disposto a spostarti."))
        Qa(L(vm, "How are rewards earned?", "Come si ottengono i premi?"),
            L(vm, "Every completed donation adds to your impact stats and unlocks badges and partner gifts.",
                "Ogni donazione completata aumenta le tue statistiche d'impatto e sblocca badge e regali dei partner."))
        Qa(L(vm, "How do I contact support?", "Come contatto il supporto?"),
            L(vm, "Email us at support@resqshare.app — we usually reply within a day.",
                "Scrivici a support@resqshare.app — di solito rispondiamo entro un giorno."))
    }
}

@Composable
fun About(vm: Vm, nav: NavHostController) {
    Scaf(vm, nav) {
        Hero(tr(vm, "About ResQShare"), L(vm, "Sharing surplus, safely.", "Condividere le eccedenze, in sicurezza."), gHero)
        Spacer(Modifier.height(16.dp))
        Pane {
            Lbl(tr(vm, "Our mission"))
            Para(L(vm,
                "ResQShare connects people with surplus food and essentials to neighbours who need them — cutting waste and helping the community at the same time.",
                "ResQShare collega chi ha cibo e beni in eccedenza ai vicini che ne hanno bisogno — riducendo gli sprechi e aiutando la comunità allo stesso tempo."))
        }
        Spacer(Modifier.height(12.dp))
        Pane {
            Lbl(tr(vm, "How it works"))
            Step(L(vm, "Donors post surplus items with an AI-built safe listing.", "I donatori pubblicano le eccedenze con un annuncio sicuro creato dall'IA."))
            Step(L(vm, "Recipients browse nearby offers and request what they need.", "I beneficiari sfogliano le offerte vicine e richiedono ciò di cui hanno bisogno."))
            Step(L(vm, "Pickup or delivery is arranged through in-app chat.", "Il ritiro o la consegna si organizzano tramite la chat nell'app."))
        }
        Spacer(Modifier.height(12.dp))
        Pane {
            Lbl(tr(vm, "Version"))
            Para("ResQShare 1.0 · STEMINATE HACKS 2026")
        }
    }
}

@Composable
fun Terms(vm: Vm, nav: NavHostController) {
    Scaf(vm, nav) {
        Hero(tr(vm, "Terms & policies"), L(vm, "The rules of using ResQShare.", "Le regole d'uso di ResQShare."), gAi)
        Spacer(Modifier.height(16.dp))
        Pane {
            Lbl(tr(vm, "Using the service"))
            Para(L(vm, "By using ResQShare you agree to provide accurate information about every item you share.",
                "Usando ResQShare accetti di fornire informazioni accurate su ogni oggetto che condividi."))
        }
        Spacer(Modifier.height(12.dp))
        Pane {
            Lbl(tr(vm, "Food safety"))
            Para(L(vm, "You must follow local food-safety rules and check items before any handover. ResQShare is not liable for items exchanged between users.",
                "Devi rispettare le norme locali sulla sicurezza alimentare e controllare gli oggetti prima di ogni consegna. ResQShare non è responsabile degli oggetti scambiati tra gli utenti."))
        }
        Spacer(Modifier.height(12.dp))
        Pane {
            Lbl(tr(vm, "Respect"))
            Para(L(vm, "No selling, no harassment, no illegal or unsafe goods. Accounts that break these rules can be banned.",
                "Niente vendite, niente molestie, nessun bene illegale o pericoloso. Gli account che violano queste regole possono essere bloccati."))
        }
    }
}

@Composable
fun Privacy(vm: Vm, nav: NavHostController) {
    Scaf(vm, nav) {
        Hero(tr(vm, "Privacy & safety"), L(vm, "How we protect you.", "Come ti proteggiamo."), gHero)
        Spacer(Modifier.height(16.dp))
        Pane {
            Lbl(tr(vm, "Your location"))
            Para(L(vm, "We never share your exact address. Others see only your general area and the radius you choose.",
                "Non mostriamo mai il tuo indirizzo esatto. Gli altri vedono solo la tua zona generale e il raggio che scegli."))
        }
        Spacer(Modifier.height(12.dp))
        Pane {
            Lbl(tr(vm, "Your data"))
            Para(L(vm, "Your email is used only to sign in. We do not sell your data to anyone.",
                "La tua email è usata solo per l'accesso. Non vendiamo i tuoi dati a nessuno."))
        }
        Spacer(Modifier.height(12.dp))
        Pane {
            Lbl(tr(vm, "Staying safe"))
            Para(L(vm, "Meet in public places when possible and trust the AI safety checklist before taking food.",
                "Quando possibile incontratevi in luoghi pubblici e fidati della checklist di sicurezza dell'IA prima di prendere il cibo."))
        }
    }
}

@Composable
fun EditProf(vm: Vm, nav: NavHostController) {
    val a = vm.acct
    var name by remember { mutableStateOf(a?.name ?: "") }
    var bio by remember { mutableStateOf("") }
    var saved by remember { mutableStateOf(false) }
    Scaf(
        vm, nav,
        foot = {
            Btn(tr(vm, "Save changes")) {
                vm.saveName(name)
                if (vm.role == "DONOR") vm.saveBio(bio)
                saved = true
            }
        }
    ) {
        Hero(tr(vm, "Edit profile"), a?.email ?: "", gAi)
        Spacer(Modifier.height(16.dp))
        Pane {
            Lbl(tr(vm, "Display name"))
            Field(tr(vm, "Display name"), name) { name = it.take(40) }
            Spacer(Modifier.height(12.dp))
            Lbl(tr(vm, "Email"))
            Text(a?.email ?: "-", color = Sub, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(4.dp))
            Text(L(vm, "Email cannot be changed.", "L'email non può essere modificata."), color = Sub, fontSize = 12.sp)
            if (vm.role == "DONOR") {
                Spacer(Modifier.height(14.dp))
                Lbl(tr(vm, "About you"))
                Field(L(vm, "Tell recipients who you are (e.g. a small bakery)", "Di' ai beneficiari chi sei (es. una piccola panetteria)"), bio) { bio = it.take(300) }
            }
        }
        if (saved) {
            Spacer(Modifier.height(12.dp))
            Text(L(vm, "Saved.", "Salvato."), color = GrnD, fontWeight = FontWeight.ExtraBold)
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
fun Places(vm: Vm, nav: NavHostController) {
    var rad by remember { mutableStateOf(vm.pRad) }
    var saved by remember { mutableStateOf(false) }
    Scaf(
        vm, nav,
        foot = { Btn(tr(vm, "Save changes")) { vm.setRad(rad); saved = true } }
    ) {
        Hero(tr(vm, "My location"), L(vm, "Your area and pickup radius.", "La tua zona e il raggio di ritiro."), gHero)
        Spacer(Modifier.height(16.dp))
        Pane {
            Lbl(tr(vm, "My area"))
            Loc(vm, vm.pArea, vm.pLat, vm.pLng, "PROF", nav)
        }
        Spacer(Modifier.height(16.dp))
        Pane {
            Lbl(tr(vm, "Max radius (km)"))
            Text(L(vm, "Offers and pickups within this distance are shown to you.",
                "Ti vengono mostrati offerte e ritiri entro questa distanza."), color = Sub, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(10.dp))
            FlowRow(horizontalArrangement = spacedBy(8.dp), verticalArrangement = spacedBy(8.dp)) {
                for (n in listOf(1, 3, 5, 10, 15, 25, 50)) {
                    Pill("$n", rad == n) { rad = n }
                }
            }
        }
        if (saved) {
            Spacer(Modifier.height(12.dp))
            Text(L(vm, "Saved.", "Salvato."), color = GrnD, fontWeight = FontWeight.ExtraBold)
        }
        Spacer(Modifier.height(8.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Rate(vm: Vm, nav: NavHostController) {
    LaunchedEffect(Unit) { vm.done = false }
    var stars by remember { mutableStateOf(5) }
    var txt by remember { mutableStateOf("") }
    Scaf(
        vm, nav,
        foot = { Btn(tr(vm, "Send rating")) { vm.rate(stars, txt) } }
    ) {
        Hero(tr(vm, "Rate the app"), L(vm, "Your feedback helps us improve.", "Il tuo feedback ci aiuta a migliorare."), gAi)
        Spacer(Modifier.height(16.dp))
        Pane {
            Lbl(tr(vm, "Your rating"))
            Row(horizontalArrangement = spacedBy(6.dp)) {
                for (i in 1..5) {
                    Text(
                        if (i <= stars) "★" else "☆",
                        fontSize = 40.sp, color = if (i <= stars) Orng else Line,
                        modifier = Modifier.clickable { stars = i }
                    )
                }
            }
            Spacer(Modifier.height(14.dp))
            Lbl(tr(vm, "Comment"))
            Field(L(vm, "What did you like or miss?", "Cosa ti è piaciuto o è mancato?"), txt) { txt = it.take(300) }
        }
        if (vm.done) {
            Spacer(Modifier.height(12.dp))
            Pane {
                Text(L(vm, "Thank you for your feedback!", "Grazie per il tuo feedback!"), color = GrnD, fontWeight = FontWeight.ExtraBold)
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}
