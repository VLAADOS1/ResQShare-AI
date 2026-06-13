package com.resq.cfg;

import com.resq.ai.AiS;
import com.resq.dom.Acct;
import com.resq.dom.Dona;
import com.resq.dom.Donor;
import com.resq.dom.Match;
import com.resq.dom.Offer;
import com.resq.dom.Org;
import com.resq.dom.Recip;
import com.resq.dom.Req;
import com.resq.dom.Reward;
import com.resq.dom.Stats;
import com.resq.dto.AnaIn;
import com.resq.dto.AnaOut;
import com.resq.dto.DonaIn;
import com.resq.repo.AcctR;
import com.resq.repo.DonorR;
import com.resq.repo.MatchR;
import com.resq.repo.OfferR;
import com.resq.repo.OrgR;
import com.resq.repo.RecipR;
import com.resq.repo.RewardR;
import com.resq.repo.StatsR;
import com.resq.svc.DonaS;
import com.resq.type.Cat;
import com.resq.type.Role;
import com.resq.type.Stat;
import com.resq.type.Ttype;
import com.resq.type.Urg;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Seed implements CommandLineRunner {

    private final DonorR donors;
    private final RecipR recips;
    private final OrgR orgs;
    private final MatchR matchs;
    private final OfferR offers;
    private final StatsR stats;
    private final DonaS dona;
    private final AiS ai;
    private final AcctR accts;
    private final RewardR rewards;
    private final com.resq.repo.ReqR reqRepo;
    private final PasswordEncoder enc;

    @Override
    public void run(String... a) {
        if (!accts.existsByEmail("123@gmail.com")) {
            accts.save(Acct.builder()
                    .email("123@gmail.com").pass(enc.encode("123456789"))
                    .role(Role.DONOR).name("Admin").area("HQ").admin(true).test(true).build());
        }
        seedRewards();
        fixCoords();
        if (donors.count() > 0) {
            return;
        }
        Long dacc = acct("Green Fork", "greenfork@resq.app", Role.DONOR, "Mountain View");
        Donor dnr = donors.save(Donor.builder()
                .acc(dacc).type("Restaurant").name("Green Fork").area("Mountain View")
                .dtype(Cat.FOOD).pref("pickup only").test(true).build());

        Long ga = acct("The Garcia family", "garcia@resq.app", Role.RECIP, "Mountain View");
        recips.save(Recip.builder().acc(ga)
                .atype("Family").area("Mountain View").lat(37.3861).lng(-122.0839)
                .radius(10).needs("Food").diet("None").avail("Evening").deliv(false).urg(Urg.MED).test(true).build());
        Long al = acct("Alex", "alex@resq.app", Role.RECIP, "Sunnyvale");
        recips.save(Recip.builder().acc(al)
                .atype("Student").area("Sunnyvale").lat(37.3688).lng(-122.0363)
                .radius(8).needs("Hygiene").diet("None").avail("Afternoon").deliv(false).urg(Urg.LOW).test(true).build());
        Long ch = acct("Mrs. Chen", "chen@resq.app", Role.RECIP, "San Jose");
        recips.save(Recip.builder().acc(ch)
                .atype("Elderly person").area("San Jose").lat(37.3382).lng(-121.8863)
                .radius(8).needs("Food").diet("No pork").avail("Morning").deliv(true).urg(Urg.HIGH).test(true).build());
        Long pa = acct("The Patel family", "patel@resq.app", Role.RECIP, "Palo Alto");
        recips.save(Recip.builder().acc(pa)
                .atype("Family").area("Palo Alto").lat(37.4419).lng(-122.1430)
                .radius(10).needs("Clothes").diet("None").avail("Weekend").deliv(false).urg(Urg.MED).test(true).build());

        req(ga, "The Garcia family", 37.3861, -122.0839, "Mountain View", Cat.FOOD,
                "A family of four would be grateful for groceries or ready meals.", "for 4 people", Urg.MED, "Evening");
        req(ch, "Mrs. Chen", 37.3382, -121.8863, "San Jose", Cat.FOOD,
                "Elderly person needs simple groceries, no pork please.", "small portion", Urg.HIGH, "Morning");
        req(al, "Alex", 37.3688, -122.0363, "Sunnyvale", Cat.HYG,
                "Student looking for basic hygiene items - soap, toothpaste.", "any", Urg.LOW, "Afternoon");
        req(pa, "The Patel family", 37.4419, -122.1430, "Palo Alto", Cat.CLOTH,
                "Family needs warm winter clothes for two children.", "kids sizes", Urg.MED, "Weekend");

        Org o1 = orgs.save(Org.builder().acc(acct("Hope Shelter", "hope@resq.app", Role.ORG, "Mountain View"))
                .otype("Shelter").name("Hope Shelter").lat(37.3905).lng(-122.0810)
                .area("Mountain View").accept("food,hyg,cloth").dist(10).cap(25).test(true).build());
        Org o2 = orgs.save(Org.builder().acc(acct("South Bay Food Bank", "foodbank@resq.app", Role.ORG, "Sunnyvale"))
                .otype("Food bank").name("South Bay Food Bank").lat(37.3700).lng(-122.0400)
                .area("Sunnyvale").accept("food,school").dist(8).cap(40).test(true).build());
        Org o3 = orgs.save(Org.builder().acc(acct("Care Center", "care@resq.app", Role.ORG, "San Jose"))
                .otype("Community center").name("Care Center").lat(37.3382).lng(-121.8863)
                .area("San Jose").accept("food,hyg,baby,cloth").dist(12).cap(30).test(true).build());
        orgs.save(Org.builder().acc(acct("Youth Hub", "youth@resq.app", Role.ORG, "Palo Alto"))
                .otype("Volunteer group").name("Youth Hub").lat(37.4419).lng(-122.1430)
                .area("Palo Alto").accept("school,cloth,baby").dist(9).cap(20).test(true).build());

        Dona d1 = add(Cat.FOOD, "15 sandwiches", "refrigerated",
                "prepared today", "today 5-7 PM", "Mountain View",
                "packaged sandwiches", dnr.getId(), dacc, "https://images.unsplash.com/photo-1528735602780-2552fd46c7af");
        Dona d2 = add(Cat.FOOD, "5 kg", "room temperature", "sealed",
                "today 6-8 PM", "Mountain View", "fresh bread loaves", dnr.getId(), dacc, "https://images.unsplash.com/photo-1509440159596-0249088772ff");
        Dona d3 = add(Cat.HYG, "8 kits", "", "new", "tomorrow morning",
                "Sunnyvale", "hygiene kits", dnr.getId(), dacc, "https://images.unsplash.com/photo-1584305574647-0cc949a2bb9f");
        Dona d4 = add(Cat.CLOTH, "3 bags", "", "good", "weekend",
                "Sunnyvale", "children winter clothes", dnr.getId(), dacc, "https://images.unsplash.com/photo-1445205170230-053b83016050");
        add(Cat.SCHOOL, "20 notebooks", "", "new", "tomorrow",
                "Mountain View", "school notebooks and supplies", dnr.getId(), dacc, "https://images.unsplash.com/photo-1531346878377-a5be20888e57");

        matchs.save(mk(d1.getId(), o1.getId(), Ttype.ORG, 94,
                "Needs ready-to-eat meals today, pickup 5-8 PM."));
        matchs.save(mk(d2.getId(), o2.getId(), Ttype.ORG, 86,
                "Accepts packaged food and supports students nearby."));
        matchs.save(mk(d3.getId(), o3.getId(), Ttype.ORG, 91,
                "Distributes hygiene kits to families in the area."));
        matchs.save(mk(d4.getId(), 4L, Ttype.RECIP, 89,
                "Family with a child needs winter clothes."));

        offers.save(Offer.builder().dona(d3.getId()).tgt(o3.getId())
                .ttype(Ttype.ORG).stat(Stat.SENT).build());
        dona.move(d3.getId(), Stat.SENT);
    }

    private void fixCoords() {
        for (com.resq.dom.Recip r : recips.findAll()) {
            if (r.getLat() == null) {
                double[] c = coords(r.getArea());
                r.setLat(c[0]);
                r.setLng(c[1]);
                recips.save(r);
            }
        }
        for (com.resq.dom.Org o : orgs.findAll()) {
            if (o.getLat() == null) {
                double[] c = coords(o.getArea());
                o.setLat(c[0]);
                o.setLng(c[1]);
                orgs.save(o);
            }
        }
    }

    private void req(Long acc, String name, double lat, double lng, String area, Cat cat,
            String descr, String qty, Urg urg, String avail) {
        reqRepo.save(Req.builder().acc(acc).name(name).lat(lat).lng(lng).area(area).test(true)
                .stat("OPEN").cat(cat).descr(descr).qty(qty).urg(urg).radius(15).avail(avail)
                .pub(descr).build());
    }

    private String fetchPic(String url) {
        try {
            java.net.http.HttpClient cli = java.net.http.HttpClient.newBuilder()
                    .followRedirects(java.net.http.HttpClient.Redirect.NORMAL)
                    .connectTimeout(java.time.Duration.ofSeconds(8)).build();
            java.net.http.HttpRequest req = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create(url + "?w=640&q=70&fit=crop"))
                    .timeout(java.time.Duration.ofSeconds(15)).GET().build();
            java.net.http.HttpResponse<byte[]> r = cli.send(req, java.net.http.HttpResponse.BodyHandlers.ofByteArray());
            if (r.statusCode() == 200 && r.body() != null && r.body().length > 800) {
                return "data:image/jpeg;base64," + java.util.Base64.getEncoder().encodeToString(r.body());
            }
        } catch (Exception e) {
        }
        return null;
    }

    private Long acct(String name, String email, Role role, String area) {
        return accts.save(Acct.builder()
                .email(email).pass(enc.encode("resq1234"))
                .role(role).name(name).area(area).test(true).build()).getId();
    }

    private double[] coords(String area) {
        String a = area == null ? "" : area.toLowerCase();
        if (a.contains("sunnyvale")) {
            return new double[]{37.3688, -122.0363};
        }
        if (a.contains("san jose")) {
            return new double[]{37.3382, -121.8863};
        }
        if (a.contains("palo alto")) {
            return new double[]{37.4419, -122.1430};
        }
        return new double[]{37.3861, -122.0839};
    }

    private void seedRewards() {
        rw("First share", "🌱", "meals", 1, null,
                "Make your very first donation and start your journey of giving.");
        rw("Getting started", "🍞", "meals", 10, null,
                "Provide 10 meals to people in your community.");
        rw("100 meals", "🍽️", "meals", 100, null,
                "Reach 100 meals shared — a real difference for many tables.");
        rw("Helping hands", "👐", "people", 25, null,
                "Help 25 different people through your donations.");
        rw("50 people helped", "🤝", "people", 50, null,
                "Support 50 people across the city with what you share.");
        rw("Eco saver", "♻️", "kg", 50, null,
                "Rescue 50 kg of food and goods from going to waste.");
        rw("Climate hero", "🌍", "co2", 100, null,
                "Prevent 100 kg of CO₂ emissions by reusing instead of trashing.");
        rw("Reuse champion", "📦", "items", 50, null,
                "Give 50 individual items a useful second life.");
        rw("5 partners", "🏅", "orgs", 5, null,
                "Collaborate with 5 partner organizations in the network.");
        rw("Coffee voucher", "☕", "meals", 200,
                "Free coffee at partner cafés",
                "Provide 200 meals to unlock a free coffee at any partner café.");
        rw("Cinema pass", "🎟️", "people", 100,
                "2 cinema tickets",
                "Help 100 people to earn 2 cinema tickets from our partners.");
        rw("Grocery card", "🛒", "kg", 100,
                "$20 grocery gift card",
                "Rescue 100 kg of goods to receive a $20 grocery gift card.");
    }

    private void rw(String title, String emoji, String metric, int goal, String gift, String descr) {
        Reward r = rewards.findByTitle(title).orElseGet(Reward::new);
        r.setTitle(title);
        r.setEmoji(emoji);
        r.setMetric(metric);
        r.setGoal(goal);
        r.setGift(gift);
        r.setDescr(descr);
        rewards.save(r);
    }

    private Dona add(Cat cat, String qty, String store, String cond,
            String pwin, String area, String notes, Long dnr, Long acc, String kw) {
        AnaIn in = AnaIn.builder().cat(cat).qty(qty).store(store).cond(cond)
                .pwin(pwin).area(area).notes(notes).build();
        AnaOut a = ai.quick(in);
        String chk = a.getChecks() == null ? "" : a.getChecks().stream()
                .map(c -> c + " ::: Y").collect(java.util.stream.Collectors.joining("\n"));
        String pic = fetchPic(kw);
        java.util.List<String> pics = pic == null
                ? new java.util.ArrayList<>() : new java.util.ArrayList<>(java.util.List.of(pic));
        return dona.create(DonaIn.builder().donor(dnr).acc(acc).dname("Green Fork").title(a.getTitle())
                .descr(a.getDescr()).cat(cat).qty(qty).store(store).cond(cond)
                .pwin(pwin).area(area).notes(notes).photo(notes)
                .safe(a.getSafe()).risk(a.getRisk()).expl(a.getExpl())
                .chk(chk).test(true).imgs(pics).build());
    }

    private Match mk(Long dn, Long tgt, Ttype tt, int sc, String why) {
        return Match.builder().dona(dn).tgt(tgt).ttype(tt).score(sc)
                .reason(why).stat(Stat.AVAIL).build();
    }
}
