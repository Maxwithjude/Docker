package com.be.byeoldam.sharedcollection;


import com.be.byeoldam.domain.sharedcollection.dto.SharedCollectionResponse;
import com.be.byeoldam.domain.sharedcollection.model.Role;
import com.be.byeoldam.domain.sharedcollection.model.SharedCollection;
import com.be.byeoldam.domain.sharedcollection.model.SharedUser;
import com.be.byeoldam.domain.sharedcollection.repository.SharedCollectionRepository;
import com.be.byeoldam.domain.sharedcollection.repository.SharedUserRepository;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import com.be.byeoldam.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SharedCollectionServiceTest {

    @Mock
    private SharedCollectionRepository sharedCollectionRepository;

    @Mock
    private SharedUserRepository sharedUserRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SharedCollectionService sharedCollectionService;

    public SharedCollectionServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSharedCollection() {
        // Given
        Long userId = 1L;
        User mockUser = mock(User.class);
        SharedCollection mockCollection1 = mock(SharedCollection.class);
        SharedCollection mockCollection2 = mock(SharedCollection.class);
        SharedUser mockSharedUser1 = mock(SharedUser.class);
        SharedUser mockSharedUser2 = mock(SharedUser.class);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(sharedUserRepository.findByUser(mockUser)).thenReturn(Arrays.asList(mockSharedUser1, mockSharedUser2));
        when(mockSharedUser1.getSharedCollection()).thenReturn(mockCollection1);
        when(mockSharedUser2.getSharedCollection()).thenReturn(mockCollection2);
        when(mockCollection1.getId()).thenReturn(1L);
        when(mockCollection1.getName()).thenReturn("Collection 1");
        when(mockCollection2.getId()).thenReturn(2L);
        when(mockCollection2.getName()).thenReturn("Collection 2");

        // When
        List<SharedCollectionResponse> responses = sharedCollectionService.getSharedCollection(userId);

        // Then
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(1L, responses.get(0).getId());
        assertEquals("Collection 1", responses.get(0).getName());
        assertEquals(2L, responses.get(1).getId());
        assertEquals("Collection 2", responses.get(1).getName());

        verify(userRepository, times(1)).findById(userId);
        verify(sharedUserRepository, times(1)).findByUser(mockUser);
    }

    @Test
    void testDeleteSharedCollection() {
        // Given
        Long userId = 1L;
        Long collectionId = 1L;
        User mockUser = mock(User.class);
        SharedCollection mockCollection = mock(SharedCollection.class);
        SharedUser mockSharedUser = mock(SharedUser.class);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(sharedCollectionRepository.findById(collectionId)).thenReturn(Optional.of(mockCollection));
        when(sharedUserRepository.findByUserAndSharedCollection(mockUser, mockCollection)).thenReturn(Optional.of(mockSharedUser));
        when(mockSharedUser.getRole()).thenReturn(Role.OWNER);

        // When
        sharedCollectionService.deleteSharedCollection(userId, collectionId);

        // Then
        verify(sharedCollectionRepository, times(1)).delete(mockCollection);
        verify(sharedUserRepository, times(1)).deleteBySharedCollection(mockCollection);
    }

    @Test
    void testDeleteSharedCollection_NotOwner() {
        // Given
        Long userId = 1L;
        Long collectionId = 1L;
        User mockUser = mock(User.class);
        SharedCollection mockCollection = mock(SharedCollection.class);
        SharedUser mockSharedUser = mock(SharedUser.class);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(sharedCollectionRepository.findById(collectionId)).thenReturn(Optional.of(mockCollection));
        when(sharedUserRepository.findByUserAndSharedCollection(mockUser, mockCollection)).thenReturn(Optional.of(mockSharedUser));
        when(mockSharedUser.getRole()).thenReturn(Role.MEMBER);

        // When / Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            sharedCollectionService.deleteSharedCollection(userId, collectionId);
        });

        assertNotNull(exception);
        verify(sharedCollectionRepository, never()).delete(mockCollection);
        verify(sharedUserRepository, never()).deleteBySharedCollection(mockCollection);
    }
}