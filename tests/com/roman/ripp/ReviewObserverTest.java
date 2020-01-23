package com.roman.ripp;

import com.roman.ripp.collab.ActionItem;
import com.roman.ripp.collab.CollabApiService;
import com.roman.ripp.collab.Participant;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewObserverTest {

    @Mock
    CollabApiService collabApiServiceMock;

    @Test
    void TestGetBadReviewersNoActionItems() {
        collabApiServiceMock = mock(CollabApiService.class);
        when(collabApiServiceMock.GetActionItems()).thenReturn(new ArrayList<ActionItem>());
        var observer = new ReviewObserver(null);
        var map = observer.GetBadReviewersAndOverdueActionItems(collabApiServiceMock);
        assertTrue(map.isEmpty());
    }

    private ArrayList<Participant> CreateFakeReviewerList() {
        var list = new ArrayList<Participant>();
        list.add(new Participant() {{
            role = "AUTHOR";
            user = "James.Bond";
        }});
        list.add(new Participant() {{
            role = "OBSERVER";
            user = "Jack.Ripper";
        }});
        list.add(new Participant() {{
            role = "OBSERVER";
            user = "Donald.Trump";
        }});
        list.add(new Participant() {{
            role = "OBSERVER";
            user = "Mr.Putin";
        }});
        list.add(new Participant() {{
            role = "REVIEWER";
            user = "Adolf.Hitler";
        }});
        return list;
    }

    @Test
    void TestGetBadReviewersOneActionItem() {
        collabApiServiceMock = mock(CollabApiService.class);
        var actionItem = new ActionItem() {{
            reviewText = "Some Review name";
            reviewId = 1;
        }};
        when(collabApiServiceMock.GetActionItems()).thenReturn(new ArrayList<ActionItem>() {{
            add(actionItem);
        }});
        when(collabApiServiceMock.GetReviewParticipants(actionItem.reviewId)).thenReturn(CreateFakeReviewerList());
        var observer = new ReviewObserver(null);
        var map = observer.GetBadReviewersAndOverdueActionItems(collabApiServiceMock);
        assertTrue(map.isEmpty());
    }

    @Test
    void TestGetBadReviewersOneOverdueActionItem() {
        collabApiServiceMock = mock(CollabApiService.class);
        var actionItem = new ActionItem() {{
            reviewText = "Some Review name (OvErDue)";
            reviewId = 1;
        }};

        when(collabApiServiceMock.GetActionItems()).thenReturn(new ArrayList<ActionItem>() {{
            add(actionItem);
        }});

        when(collabApiServiceMock.GetReviewParticipants(actionItem.reviewId)).thenReturn(CreateFakeReviewerList());

        var observer = new ReviewObserver(null);
        var map = observer.GetBadReviewersAndOverdueActionItems(collabApiServiceMock);

        assertEquals(map.size(), 1);
        assertTrue(map.containsKey("Adolf.Hitler"));
        assertEquals(map.get("Adolf.Hitler").size(), 1);
        assertEquals(1, map.get("Adolf.Hitler").get(0).reviewId);
    }

    private ArrayList<ActionItem> CreateFakeActionItemsList() {
        var list = new ArrayList<ActionItem>(){{
                add(new ActionItem(){{ reviewText = "Review__1"; reviewId = 1; }});
                add(new ActionItem(){{ reviewText = "(overDue)"; reviewId = 2; }});
                add(new ActionItem(){{ reviewText = "Review__3"; reviewId = 3; }});
                add(new ActionItem(){{ reviewText = "(overdue)"; reviewId = 4; }});
                add(new ActionItem(){{ reviewText = "(OVERDUE)"; reviewId = 5; }});
                add(new ActionItem(){{ reviewText = "Review__6"; reviewId = 6; }});
        }};
        return list;
    }

    @Test
    void TestGetBadReviewersFewOverdueActionItem() {
        collabApiServiceMock = mock(CollabApiService.class);
        when(collabApiServiceMock.GetActionItems()).thenReturn(CreateFakeActionItemsList());
        when(collabApiServiceMock.GetReviewParticipants(anyInt())).thenReturn(CreateFakeReviewerList());

        var observer = new ReviewObserver(null);
        var map = observer.GetBadReviewersAndOverdueActionItems(collabApiServiceMock);
        assertEquals(map.size(), 1);
        assertEquals(map.get("Adolf.Hitler").size(), 3);
    }
}