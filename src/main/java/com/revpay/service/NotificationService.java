package com.revpay.service;

import com.revpay.exception.ResourceNotFoundException;
import com.revpay.model.entity.Notification;
import com.revpay.repository.NotificationRepository;
import com.revpay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.revpay.model.entity.User;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;
    private final UserRepository userRepository;

    @Transactional
    public void createNotification(Long userId, String message, String type) {
        log.info("NOTIFICATION_CREATE_INIT | UserID: {} | Type: {}", userId, type);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check preference
        if (user.getDisabledNotificationTypes().contains(type.toUpperCase())) {
            log.debug("NOTIFICATION_SKIPPED_DUE_TO_PREFERENCE | UserID: {} | Type: {}", userId, type);
            return;
        }

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setType(type);

        repository.save(notification);

        log.debug("NOTIFICATION_CREATED_SUCCESS | UserID: {} | Type: {}", userId, type);
    }

    @Transactional(readOnly = true)
    public List<Notification> getUserNotifications(Long userId) {
        log.info("NOTIFICATION_FETCH_ALL | UserID: {}", userId);
        List<Notification> notifications = repository.findByUserIdOrderByCreatedAtDesc(userId);
        log.debug("Fetched {} notifications for UserID: {}", notifications.size(), userId);
        return notifications;
    }

    @Transactional(readOnly = true)
    public Page<Notification> getUserNotificationsPaged(Long userId, Pageable pageable) {
        log.info("NOTIFICATION_FETCH_PAGED | UserID: {}", userId);
        return repository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        log.info("NOTIFICATION_MARK_READ_INIT | NotificationID: {}", notificationId);

        Notification notification = repository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + notificationId));

        // Idempotency: Prevent redundant database writes if already read
        if (notification.isRead()) {
            log.debug("NOTIFICATION_IDEMPOTENCY | Notification {} is already read", notificationId);
            return;
        }

        notification.setRead(true);
        repository.save(notification);

        log.info("NOTIFICATION_MARKED_READ | NotificationID: {}", notificationId);
    }

    @Transactional
    public void updateNotificationPreference(Long userId, String type, boolean enabled) {
        log.info("NOTIFICATION_PREF_UPDATE_INIT | UserID: {} | Type: {} | Enabled: {}", userId, type, enabled);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (enabled) {
            user.getDisabledNotificationTypes().remove(type.toUpperCase());
        } else {
            user.getDisabledNotificationTypes().add(type.toUpperCase());
        }

        userRepository.save(user);

        log.info("NOTIFICATION_PREF_UPDATE_SUCCESS | UserID: {} | Type: {} | Enabled: {}", userId, type, enabled);
    }
}