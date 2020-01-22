package com.roman.ripp;

import com.roman.ripp.collab.ActionItem;
import com.roman.ripp.collab.CollabApiService;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewObserverTest {

    //@Rule
    //public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    CollabApiService collabApiServiceMock;

    @Test
    void TestNoActionItems() {
        var observer = new ReviewObserver(null);
        collabApiServiceMock = mock(CollabApiService.class);
        when(collabApiServiceMock.GetActionItems()).thenReturn(new ArrayList<ActionItem>());
        var map = observer.GetBadReviewersAndOverdueReviews(collabApiServiceMock);
        assertTrue(map.isEmpty());
    }
}