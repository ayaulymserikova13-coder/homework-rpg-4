package com.narxoz.rpg.battle;

import com.narxoz.rpg.bridge.Skill;
import com.narxoz.rpg.composite.CombatNode;

import java.util.Random;

public class RaidEngine {
    private Random random = new Random(1L);

    public RaidEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public RaidResult runRaid(CombatNode teamA, CombatNode teamB, Skill teamASkill, Skill teamBSkill) {
        RaidResult result = new RaidResult();

        if (teamA == null || teamB == null || teamASkill == null || teamBSkill == null) {
            result.setRounds(0);
            result.setWinner("Invalid");
            result.addLine("Invalid input: null team or skill.");
            return result;
        }

        if (!teamA.isAlive() && !teamB.isAlive()) {
            result.setRounds(0);
            result.setWinner("Draw");
            result.addLine("Both teams are already defeated.");
            return result;
        }

        if (!teamA.isAlive()) {
            result.setRounds(0);
            result.setWinner(teamB.getName());
            result.addLine(teamA.getName() + " is already defeated.");
            return result;
        }

        if (!teamB.isAlive()) {
            result.setRounds(0);
            result.setWinner(teamA.getName());
            result.addLine(teamB.getName() + " is already defeated.");
            return result;
        }

        int rounds = 0;
        int maxRounds = 100;

        while (teamA.isAlive() && teamB.isAlive() && rounds < maxRounds) {
            rounds++;
            result.addLine("Round " + rounds + ":");

            if (teamA.isAlive()) {
                int before = teamB.getHealth();
                teamASkill.cast(teamB);
                int dealt = before - teamB.getHealth();
                result.addLine("  " + teamA.getName() + " uses " + teamASkill.getSkillName()
                        + " (" + teamASkill.getEffectName() + ") on " + teamB.getName()
                        + " for " + dealt + " damage.");
            }

            if (teamB.isAlive()) {
                int before = teamA.getHealth();
                teamBSkill.cast(teamA);
                int dealt = before - teamA.getHealth();
                result.addLine("  " + teamB.getName() + " uses " + teamBSkill.getSkillName()
                        + " (" + teamBSkill.getEffectName() + ") on " + teamA.getName()
                        + " for " + dealt + " damage.");
            }

            result.addLine("  Status: "
                    + teamA.getName() + " HP=" + teamA.getHealth()
                    + ", " + teamB.getName() + " HP=" + teamB.getHealth());
        }

        result.setRounds(rounds);

        if (teamA.isAlive() && !teamB.isAlive()) {
            result.setWinner(teamA.getName());
        } else if (teamB.isAlive() && !teamA.isAlive()) {
            result.setWinner(teamB.getName());
        } else if (teamA.getHealth() > teamB.getHealth()) {
            result.setWinner(teamA.getName());
        } else if (teamB.getHealth() > teamA.getHealth()) {
            result.setWinner(teamB.getName());
        } else {
            result.setWinner("Draw");
        }

        return result;
    }
}
