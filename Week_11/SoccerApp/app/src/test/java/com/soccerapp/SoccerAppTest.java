package com.soccerapp;

import com.soccerapp.data.DataProvider;
import com.soccerapp.iterator.MatchIterator;
import com.soccerapp.iterator.TeamIterator;
import com.soccerapp.model.Match;
import com.soccerapp.model.Player;
import com.soccerapp.model.Team;
import com.soccerapp.repository.MatchRepository;
import com.soccerapp.repository.PlayerRepository;
import com.soccerapp.repository.Repository;
import com.soccerapp.repository.TeamRepository;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * Unit tests covering:
 *  - Generic Repository CRUD and filtering
 *  - TeamRepository domain-specific queries
 *  - PlayerRepository domain-specific queries
 *  - MatchRepository domain-specific queries
 *  - TeamIterator / MatchIterator correctness
 *  - Model validation (constructor guards)
 *  - DataProvider data integrity
 */
public class SoccerAppTest {

    private DataProvider dataProvider;
    private TeamRepository teamRepo;
    private PlayerRepository playerRepo;
    private MatchRepository matchRepo;

    @Before
    public void setUp() {
        dataProvider = new DataProvider();
        teamRepo   = new TeamRepository(dataProvider.createSampleTeams());
        playerRepo = new PlayerRepository(dataProvider.createSamplePlayers());
        matchRepo  = new MatchRepository(dataProvider.createSampleMatches());
    }

    // -----------------------------------------------------------------------
    // Generic Repository
    // -----------------------------------------------------------------------

    @Test
    public void repository_getAll_returnsAllItems() {
        assertEquals(8, teamRepo.size());
        assertEquals(12, playerRepo.size());
        assertEquals(8, matchRepo.size());
    }

    @Test
    public void repository_add_increasesSize() {
        Repository<Team> repo = new Repository<>();
        assertEquals(0, repo.size());
        repo.add(new Team("Test FC", "Finland", "Veikkausliiga", "Test Arena", 2000));
        assertEquals(1, repo.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void repository_add_null_throwsException() {
        teamRepo.add(null);
    }

    @Test
    public void repository_filter_lambda_returnsCorrectSubset() {
        // Lambda Predicate passed directly – tests the generic filter()
        List<Team> german = teamRepo.filter(t -> t.getCountry().equals("Germany"));
        assertEquals(1, german.size());
        assertEquals("Bayern Munich", german.get(0).getName());
    }

    @Test
    public void repository_searchByName_caseInsensitive() {
        List<Team> result = teamRepo.searchByName("barca");
        assertFalse(result.isEmpty());
        assertEquals("FC Barcelona", result.get(0).getName());
    }

    @Test
    public void repository_searchByName_emptyQuery_returnsAll() {
        List<Team> result = teamRepo.searchByName("");
        assertEquals(8, result.size());
    }

    @Test
    public void repository_findById_returnsCorrectItem() {
        Team barcelona = teamRepo.getAll().get(0);
        Team found = teamRepo.findById(barcelona.getId());
        assertNotNull(found);
        assertEquals("FC Barcelona", found.getName());
    }

    @Test
    public void repository_findById_unknownId_returnsNull() {
        assertNull(teamRepo.findById("nonexistent-id"));
    }

    // -----------------------------------------------------------------------
    // TeamRepository
    // -----------------------------------------------------------------------

    @Test
    public void teamRepo_filterByLeague_laLiga() {
        List<Team> result = teamRepo.filterByLeague("La Liga");
        assertEquals(1, result.size());
        assertEquals("FC Barcelona", result.get(0).getName());
    }

    @Test
    public void teamRepo_filterByLeague_emptyQuery_returnsAll() {
        List<Team> result = teamRepo.filterByLeague("");
        assertEquals(8, result.size());
    }

    @Test
    public void teamRepo_sortedByName_isAlphabetical() {
        List<Team> sorted = teamRepo.sortedByName();
        for (int i = 0; i < sorted.size() - 1; i++) {
            assertTrue(sorted.get(i).getName()
                    .compareToIgnoreCase(sorted.get(i + 1).getName()) <= 0);
        }
    }

    @Test
    public void teamRepo_sortedByFounded_isAscending() {
        List<Team> sorted = teamRepo.sortedByFounded();
        for (int i = 0; i < sorted.size() - 1; i++) {
            assertTrue(sorted.get(i).getFounded() <= sorted.get(i + 1).getFounded());
        }
    }

    @Test
    public void teamRepo_filterByCountry() {
        List<Team> result = teamRepo.filterByCountry("France");
        assertEquals(1, result.size());
        assertEquals("Paris Saint-Germain", result.get(0).getName());
    }

    // -----------------------------------------------------------------------
    // PlayerRepository
    // -----------------------------------------------------------------------

    @Test
    public void playerRepo_filterByTeam_bayernMunich() {
        List<Player> result = playerRepo.filterByTeam("Bayern Munich");
        assertEquals(3, result.size()); // Lewandowski, Neuer, Kimmich
    }

    @Test
    public void playerRepo_filterByPosition_forwards() {
        List<Player> forwards = playerRepo.filterByPosition("Forward");
        assertFalse(forwards.isEmpty());
        for (Player p : forwards) {
            assertEquals("Forward", p.getPosition());
        }
    }

    @Test
    public void playerRepo_sortedByAge_isAscending() {
        List<Player> sorted = playerRepo.sortedByAge();
        for (int i = 0; i < sorted.size() - 1; i++) {
            assertTrue(sorted.get(i).getAge() <= sorted.get(i + 1).getAge());
        }
    }

    // -----------------------------------------------------------------------
    // MatchRepository
    // -----------------------------------------------------------------------

    @Test
    public void matchRepo_filterByTeam_barcelona() {
        List<Match> result = matchRepo.filterByTeam("FC Barcelona");
        assertEquals(2, result.size()); // vs Real Madrid, vs Bayern Munich
    }

    @Test
    public void matchRepo_filterByLeague_championsLeague() {
        List<Match> result = matchRepo.filterByLeague("Champions League");
        assertEquals(3, result.size());
    }

    @Test
    public void matchRepo_sortedByDate_isChronological() {
        List<Match> sorted = matchRepo.sortedByDate();
        for (int i = 0; i < sorted.size() - 1; i++) {
            assertTrue(sorted.get(i).getDate().compareTo(sorted.get(i + 1).getDate()) <= 0);
        }
    }

    // -----------------------------------------------------------------------
    // TeamIterator
    // -----------------------------------------------------------------------

    @Test
    public void teamIterator_traversesAllItems() {
        List<Team> teams = teamRepo.getAll();
        TeamIterator iterator = new TeamIterator(teams);
        int count = 0;
        while (iterator.hasNext()) {
            assertNotNull(iterator.next());
            count++;
        }
        assertEquals(teams.size(), count);
    }

    @Test
    public void teamIterator_reset_allowsReTraversal() {
        List<Team> teams = teamRepo.getAll();
        TeamIterator iterator = new TeamIterator(teams);
        // Exhaust
        while (iterator.hasNext()) iterator.next();
        assertFalse(iterator.hasNext());
        // Reset and traverse again
        iterator.reset();
        assertTrue(iterator.hasNext());
        assertEquals(teams.get(0).getName(), iterator.next().getName());
    }

    @Test(expected = NoSuchElementException.class)
    public void teamIterator_next_afterExhaustion_throwsException() {
        TeamIterator iterator = new TeamIterator(teamRepo.getAll());
        while (iterator.hasNext()) iterator.next();
        iterator.next(); // should throw
    }

    @Test(expected = IllegalArgumentException.class)
    public void teamIterator_nullList_throwsException() {
        new TeamIterator(null);
    }

    // -----------------------------------------------------------------------
    // MatchIterator
    // -----------------------------------------------------------------------

    @Test
    public void matchIterator_traversesAllItems() {
        List<Match> matches = matchRepo.getAll();
        MatchIterator iterator = new MatchIterator(matches);
        int count = 0;
        while (iterator.hasNext()) {
            assertNotNull(iterator.next());
            count++;
        }
        assertEquals(matches.size(), count);
    }

    // -----------------------------------------------------------------------
    // Model Validation
    // -----------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void team_emptyName_throwsException() {
        new Team("", "Finland", "Veikkausliiga", "Arena", 2000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void player_invalidAge_throwsException() {
        new Player("Test Player", 0, "Finland", "Forward", "Test FC", 9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void player_invalidJerseyNumber_throwsException() {
        new Player("Test Player", 25, "Finland", "Forward", "Test FC", 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void match_emptyHomeTeam_throwsException() {
        new Match("", "Away FC", "1-0", "League", "2024-01-01", "Arena");
    }

    // -----------------------------------------------------------------------
    // DataProvider data integrity
    // -----------------------------------------------------------------------

    @Test
    public void dataProvider_allEntitiesHaveNonNullIds() {
        teamRepo.getAll().forEach(t   -> assertNotNull(t.getId()));
        playerRepo.getAll().forEach(p -> assertNotNull(p.getId()));
        matchRepo.getAll().forEach(m  -> assertNotNull(m.getId()));
    }

    @Test
    public void dataProvider_matchInvolvesTeam_correct() {
        Match clásico = matchRepo.filterByTeam("FC Barcelona").get(0);
        assertTrue(clásico.involvesTeam("FC Barcelona"));
        assertFalse(clásico.involvesTeam("Juventus"));
    }
}
