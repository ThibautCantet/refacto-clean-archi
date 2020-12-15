package com.cantet.refacto.user.dao;

import com.cantet.refacto.user.dao.impl.MongoMovementDAO;
import com.cantet.refacto.user.model.MovementModel;
import com.cantet.refacto.user.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MongoMovementDAOTest {

    private MongoMovementDAO mongoMovementDAOImpl;

    @Mock
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoMovementDAOImpl = new MongoMovementDAO(mongoTemplate);
    }

    @Test
    public void getCredits_should_return_all_movement() {
        // given
        final Integer userId = 42;
        final MovementModel movementModel1 = new MovementModel(1, userId, 1f);
        final MovementModel movementModel2 = new MovementModel(2, userId, 2f);
        final List<MovementModel> movementModels = asList(
                movementModel1,
                movementModel2,
                new MovementModel(3, 999, 2f));
        when(mongoTemplate.findAll(MovementModel.class)).thenReturn(movementModels);

        UserModel userModel = new UserModel(userId);

        // when
        final List<MovementModel> movements = mongoMovementDAOImpl.getCredits(userModel);

        // then
        assertThat(movements).hasSize(2);
        assertThat(movements).containsExactly(movementModel1, movementModel2);

    }
}